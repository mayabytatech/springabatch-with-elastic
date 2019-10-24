
package com.illud.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.illud.domain.DrivoData;
import com.illud.repository.DrivoDataRepository;
import com.illud.service.dto.DrivoDataDTO;
import com.illud.service.dto.DrivoDump;
import com.illud.service.mapper.DrivoDataMapper;
import com.illud.web.rest.DrivoDataResource;


@Component
public class DBWriter implements ItemWriter<DrivoDump> {

	private final Logger log = LoggerFactory.getLogger(DBWriter.class);

	@Autowired
	private DrivoDataRepository drivoRepo;

	@Autowired
	private DrivoDataResource drivoResource;

	@Autowired
	private DrivoDataMapper drivoMapper;

	@Override
	public void write(List<? extends DrivoDump> itemDrive) throws Exception {

		for (DrivoDump drivodata : itemDrive) {

			System.out.println("...................... data......... "+drivodata);
			DrivoData data = new DrivoData();

			data.setMobileNo(drivodata.getMobileNo());

			data.setOwnerName(drivodata.getOwnerName());

			data.setRegNo(drivodata.getRegNo());

			data.setVehdecscr(drivodata.getVehdecscr());

			DrivoDataDTO drivoDTO = drivoMapper.toDto(data);

			drivoDTO = drivoResource.createDrivoData(drivoDTO).getBody();

		}

	}
}
