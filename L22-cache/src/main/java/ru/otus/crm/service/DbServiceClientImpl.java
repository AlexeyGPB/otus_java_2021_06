package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.crm.model.Client;
import ru.otus.core.sessionmanager.TransactionManager;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final MyCache<String, Client> myCache = new MyCache<>();

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", clientCloned);
            return clientCloned;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cachedClient = myCache.get(String.valueOf(id));
        if (cachedClient != null) {
            log.info("Cached client: {}", cachedClient);
            return Optional.of(cachedClient);
        }
        log.info("Cached client was not found with key: {}", id);
        Optional<Client> clientOptional = getClientDateBase(id);
        clientOptional.ifPresent(this::fillInCache);

        return clientOptional;
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("ClientList:{}", clientList);
            return clientList;
        });
    }

    @Override
    public Optional<Client> getClientDateBase(long id) {
        return transactionManager.doInTransaction(session -> {
            var clientList = clientDataTemplate.findById(session, id);
            log.info("Client: {}", clientList);
            return clientList;
        });
    }

    private void fillInCache(Client client) {
        myCache.put(String.valueOf(client.getId()), client);
        myCache.addListener(new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        });
    }
}
