package edu.sharif.math.yaadmaan.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sharif.math.yaadmaan.domain.Memory;
import edu.sharif.math.yaadmaan.repository.MemoryRepository;
import edu.sharif.math.yaadmaan.repository.UserPerDepartmentRepository;
import edu.sharif.math.yaadmaan.service.MemoryService;
import edu.sharif.math.yaadmaan.service.UserPerDepartmentService;
import edu.sharif.math.yaadmaan.service.dto.MemoryDTO;
import edu.sharif.math.yaadmaan.service.mapper.MemoryMapper;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Memory}.
 */
@Service
@Transactional
public class MemoryServiceImpl implements MemoryService {

    private final Logger log = LoggerFactory.getLogger(MemoryServiceImpl.class);

    private final MemoryRepository memoryRepository;

    private final UserPerDepartmentRepository userPerDepartmentRepository;
    private final UserPerDepartmentService userPerDepartmentService;
    private final MemoryMapper memoryMapper;

    public MemoryServiceImpl(MemoryRepository memoryRepository,
	    MemoryMapper memoryMapper,
	    UserPerDepartmentService userPerDepartmentService,
	    UserPerDepartmentRepository userPerDepartmentRepository) {
	this.memoryRepository = memoryRepository;
	this.userPerDepartmentRepository = userPerDepartmentRepository;
	this.userPerDepartmentService = userPerDepartmentService;
	this.memoryMapper = memoryMapper;
    }

    @Override
    public MemoryDTO save(MemoryDTO memoryDTO) {
	log.debug("Request to save Memory : {}", memoryDTO);
	Memory memory = memoryMapper.toEntity(memoryDTO);
	memory = memoryRepository.save(memory);
	return memoryMapper.toDto(memory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemoryDTO> findAll(Pageable pageable) {
	log.debug("Request to get all Memories");
	return memoryRepository.findAll(pageable).map(memoryMapper::toDto);
    }

    public Page<MemoryDTO> findAllWithEagerRelationships(Pageable pageable) {
	return memoryRepository.findAllWithEagerRelationships(pageable)
		.map(memoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MemoryDTO> findOne(Long id) {
	log.debug("Request to get Memory : {}", id);
	return memoryRepository.findOneWithEagerRelationships(id)
		.map(memoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
	log.debug("Request to delete Memory : {}", id);
	memoryRepository.deleteById(id);
    }

    @Override
    public Page<MemoryDTO> findAllInDepartment(Long depid, Pageable pageable) {
	var t = this.userPerDepartmentService.getCurrentUserInDep(depid);
	var cupd = this.userPerDepartmentRepository.findById(t.getId()).get();
	return memoryRepository.findAllWithDepartmentId(depid, cupd, pageable)
		.map(memoryMapper::toDto);
    }

    @Override
    public Page<MemoryDTO> findAllWithCurrentUserTagedIn(Long depid,
	    Pageable pageable) {
	var t = this.userPerDepartmentService.getCurrentUserInDep(depid);
	var cupd = this.userPerDepartmentRepository.findById(t.getId()).get();

	return this.memoryRepository
		.findAllWithCurrentUserTagedIn(depid, cupd, pageable)
		.map(memoryMapper::toDto);
    }

    @Override
    public Page<MemoryDTO> findAllWithUserTagedIn(Long depid, Long userInDepId,
	    Pageable pageable) {
	var t = this.userPerDepartmentService.getCurrentUserInDep(depid);
	var cupd = this.userPerDepartmentRepository.findById(t.getId()).get();

	var upd = this.userPerDepartmentRepository.findById(depid).get();
	return this.memoryRepository
		.findAllWithUserTagedIn(depid, cupd, upd, pageable)
		.map(memoryMapper::toDto);
    }
}
