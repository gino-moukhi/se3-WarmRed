package be.kdg.se3.warmred.generator.adapters;

import be.kdg.se3.warmred.generator.domain.dto.CancelOrderMessageDto;
import be.kdg.se3.warmred.generator.domain.dto.CreateOrderMessageDto;
import be.kdg.se3.warmred.generator.domain.dto.Dto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * A class that formats messages to their appropriate type for the message broker
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public class XmlMessageFormatter implements MessageFormatter {
    private final Logger logger = LoggerFactory.getLogger(XmlMessageFormatter.class);

    @Override
    public String format(Dto dto) throws FormatterException {
        String xmlResult = "Default xml result";
        try {
            JAXBContext context;
            Marshaller marshaller;
            StringWriter stringWriter = new StringWriter();
            logger.info("Formatting a DTO to an XML string");
            if (dto instanceof CreateOrderMessageDto) {
                logger.info("The message to format is of type:'CreateOrderMessageDto'");
                context = JAXBContext.newInstance(CreateOrderMessageDto.class);
            } else if (dto instanceof CancelOrderMessageDto) {
                logger.info("The message to format is of type:'CancelOrderMessageDto'");
                context = JAXBContext.newInstance(CancelOrderMessageDto.class);
            } else {
                throw new FormatterException("The given instance has not been added to the XmlMessageFormatter");
            }

            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(dto, stringWriter);
            xmlResult = stringWriter.toString();
        } catch (JAXBException e) {
            logger.error("Something went wrong with the conversion from dto to xml, a JAXB exception occurred");
        } catch (FormatterException e) {
            logger.error("The given instance has not been added to the XmlMessageFormatter");
        }
        return xmlResult;
    }
}
