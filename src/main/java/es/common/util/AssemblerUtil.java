package es.common.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import es.common.dto.AbstractCommonDto;
import es.common.entity.AbstractCommonEntity;

public class AssemblerUtil {

	
	public static void copyBasicPropertiesToEntity(AbstractCommonDto dto, AbstractCommonEntity<?> entity) {
		
		if (StringUtils.isNotBlank(dto.getCreatedAt()))
			entity.setCreatedAt(ZonedDateTime.parse(dto.getCreatedAt(), DateTimeFormatter.ISO_DATE_TIME));

		if (StringUtils.isNotBlank(dto.getModifiedAt()))
			entity.setModifiedAt(ZonedDateTime.parse(dto.getModifiedAt(), DateTimeFormatter.ISO_DATE_TIME));
		
	}
	
	public static void copyBasicPropertiesToDto(AbstractCommonEntity<?> entity, AbstractCommonDto dto) {
		
		if (entity.getCreatedAt() != null)
			dto.setCreatedAt(entity.getCreatedAt().format(DateTimeFormatter.ISO_INSTANT));

		if (entity.getModifiedAt() != null)
			dto.setModifiedAt(entity.getModifiedAt().format(DateTimeFormatter.ISO_INSTANT));
		
	}
}
