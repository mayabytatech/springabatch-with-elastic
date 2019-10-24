
package com.illud.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.illud.service.dto.DrivoDump;


@Component
public class Processor implements ItemProcessor<DrivoDump, DrivoDump> {

	@Override
	public DrivoDump process(DrivoDump drivo) throws Exception {
		System.out.println("..................");
		return drivo;
	}

}
