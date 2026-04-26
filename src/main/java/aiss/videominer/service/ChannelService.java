package aiss.videominer.service;

import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {

    @Autowired
    ChannelRepository repository;

    public List<Channel> getAllChannels() {
        return repository.findAll();
    }

    public Optional<Channel> getChannelById(String id) {
        return repository.findById(id);
    }

    // Este es el método clave que usarán los Miners para inyectar datos
    public Channel saveChannel(Channel channel) {
        return repository.save(channel);
    }
}