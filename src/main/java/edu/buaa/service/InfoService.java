package edu.buaa.service;

import edu.buaa.domain.Info;
import edu.buaa.repository.InfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Info.
 */
@Service
@Transactional
public class InfoService {

    private final Logger log = LoggerFactory.getLogger(InfoService.class);

    private final InfoRepository infoRepository;

    public InfoService(InfoRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    /**
     * Save a info.
     *
     * @param info the entity to save
     * @return the persisted entity
     */
    public Info save(Info info) {
        log.debug("Request to save Info : {}", info);
        return infoRepository.save(info);
    }

    /**
     * Get all the infos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Info> findAll(Pageable pageable) {
        log.debug("Request to get all Infos");
        return infoRepository.findAll(pageable);
    }


    /**
     * Get one info by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Info> findOne(Long id) {
        log.debug("Request to get Info : {}", id);
        return infoRepository.findById(id);
    }

    /**
     * Delete the info by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Info : {}", id);
        infoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Info> findAllInfo() {
        log.debug("Request to get all Infos");
        return infoRepository.findAll();
    }
}
