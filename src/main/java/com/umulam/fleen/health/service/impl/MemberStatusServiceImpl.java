package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.exception.memberstatus.MemberStatusCodeDuplicateException;
import com.umulam.fleen.health.exception.memberstatus.MemberStatusNotFoundException;
import com.umulam.fleen.health.model.domain.MemberStatus;
import com.umulam.fleen.health.model.dto.memberstatus.MemberStatusDto;
import com.umulam.fleen.health.model.dto.memberstatus.UpdateMemberStatusDto;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;
import com.umulam.fleen.health.repository.jpa.MemberStatusJpaRepository;
import com.umulam.fleen.health.service.MemberStatusService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MemberStatusServiceImpl implements MemberStatusService {

  private final MemberStatusJpaRepository repository;
  private final ModelMapper modelMapper;

  public MemberStatusServiceImpl(MemberStatusJpaRepository repository,
                            ModelMapper mapper) {
    this.repository = repository;
    this.modelMapper = mapper;
  }

  @Override
  @Transactional(readOnly = true)
  public MemberStatus getMemberStatus(Long id) {
    return repository
            .findById(id)
            .orElseThrow(() -> new MemberStatusNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public MemberStatus getMemberStatusByCode(String code) {
    return repository
            .findByCode(code)
            .orElseThrow(() -> new MemberStatusNotFoundException(code));
  }

  @Override
  @Transactional(readOnly = true)
  public MemberStatus getReference(Long id) {
    return Optional
            .of(repository.getReferenceById(id))
            .orElseThrow(() -> new MemberStatusNotFoundException(id));
  }

  @Override
  public List<MemberStatus> getMemberStatuses() {
    return repository.findAll();
  }

  @Override
  @Transactional
  public MemberStatus saveMemberStatus(MemberStatusDto dto) {
    MemberStatus memberStatus = dto.toMemberStatus();
    return repository.save(memberStatus);
  }

  @Override
  @Transactional
  public MemberStatus updateMemberStatus(Long id, UpdateMemberStatusDto dto) {
    getMemberStatus(id);
    MemberStatus memberStatus = getMemberStatusByCode(dto.getCode());
    if (isMemberStatusExistsByCode(dto.getCode()) && !(memberStatus.getId().intValue() == id.intValue())) {
      throw new MemberStatusCodeDuplicateException(dto.getCode());
    }

    modelMapper.map(dto, memberStatus);
    return repository.save(memberStatus);
  }

  @Override
  @Transactional
  public void deleteMany(DeleteIdsDto dto) {
    List<MemberStatus> countries = dto
            .getIds()
            .stream()
            .map(id -> MemberStatus.builder()
                    .id(id).build())
            .collect(Collectors.toList());

    repository.deleteAll(countries);
  }

  @Override
  public void deleteAllMemberStatus() {
    repository.deleteAll();
  }

  @Override
  public boolean isMemberStatusExists(Long id) {
    return repository
            .findById(id)
            .isPresent();
  }

  @Override
  public boolean isMemberStatusExistsByCode(String code) {
    return repository
            .findByCode(code)
            .isPresent();
  }
}
 
