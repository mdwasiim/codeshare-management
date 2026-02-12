package com.codeshare.airline.ssim.ingestion.parser;


import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFile;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundHeader;
import org.springframework.stereotype.Component;

@Component
public class T1HeaderParser {

    public SsimInboundHeader parse(
            String line,
            SsimInboundFile inboundFile
    ) {

        if (line == null || line.length() != 200) {
            throw new IllegalStateException("Invalid SSIM record length");
        }

        if (line.charAt(0) != '1') {
            throw new IllegalStateException("Not a T1 record");
        }

        SsimInboundHeader header = new SsimInboundHeader();
        header.setFile(inboundFile);
        header.setRecordType("1");
        header.setTitleOfContents(line.substring(1, 36));
        header.setDatasetSerialNumber(line.substring(36, 39));
        header.setRecordSerialNumber(line.substring(39, 45));
        header.setRawRecord(line);

        return header;
    }
}
