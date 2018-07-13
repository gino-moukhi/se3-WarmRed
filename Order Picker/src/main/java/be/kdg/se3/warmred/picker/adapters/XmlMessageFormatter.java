package be.kdg.se3.warmred.picker.adapters;

import be.kdg.se3.warmred.picker.domain.dto.*;
import be.kdg.se3.warmred.picker.domain.dto.ExtraCreateOrderMessageDto;
import be.kdg.se3.warmred.picker.domain.dto.MessageDto;
import be.kdg.se3.warmred.picker.exceptions.FormatterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * A class that formats messages to their appropriate type for the message broker
 *
 * @author Gino Moukhi
 * @version 1.0.0
 */
public class XmlMessageFormatter implements MessageFormatter {
    private final Logger logger = LoggerFactory.getLogger(XmlMessageFormatter.class);
    private JAXBContext context;

    /**
     * This method is for sending an XML string to the message broker
     * @param messageDto
     * @return
     */
    @Override
    public String format(MessageDto messageDto) {
        String xmlResult = "Default xml result";
        try {
            Marshaller marshaller;
            StringWriter stringWriter = new StringWriter();
            logger.info("Formatting a DTO to an XML string");
            if (messageDto instanceof CreateOrderMessageDto) {
                logger.info("The message to format is of type:'CreateOrderMessageDto'");
                context = JAXBContext.newInstance(CreateOrderMessageDto.class);
            } else if (messageDto instanceof CancelOrderMessageDto) {
                logger.info("The message to format is of type:'CancelOrderMessageDto'");
                context = JAXBContext.newInstance(CancelOrderMessageDto.class);
            } else if (messageDto instanceof ExtraCreateOrderMessageDto) {
                logger.info("The message to format is of type:'ExtraCreateOrderMessageDto'");
                context = JAXBContext.newInstance(ExtraCreateOrderMessageDto.class);
            }else {
                throw new FormatterException("The given instance has not been added to the XmlMessageFormatter");
            }

            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(messageDto, stringWriter);
            xmlResult = stringWriter.toString();
        } catch (JAXBException e) {
            logger.error("Something went wrong with the conversion from messageDto to xml, a JAXB exception occurred " , e);
        } catch (FormatterException e) {
            logger.error("The given instance has not been added to the XmlMessageFormatter");
        }
        return xmlResult;
    }

    /**
     * This method is for receiving an XML string from the message broker
     * @param xmlText
     * @return
     */
    @Override
    public MessageDto format(String xmlText) throws FormatterException {
        Document xmlDocument;
        try {
            logger.info("Formatting an XML string to an DTO object");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xmlText));
            xmlDocument = builder.parse(inputSource);
        }catch (Exception e){
            throw new FormatterException("Error converting Xml string to a document ", e);
        }

        String rootName = xmlDocument.getDocumentElement().getTagName();
        MessageDto output;

        try {
            Unmarshaller unmarshaller;
            if (rootName.contains("createOrderMessage")) {
                logger.info("The message to format is of type:'CreateOrderMessageDto'");
                context = JAXBContext.newInstance(CreateOrderMessageDto.class);
                unmarshaller = context.createUnmarshaller();
                Reader reader = new StringReader(xmlText);
                output = (CreateOrderMessageDto) unmarshaller.unmarshal(reader);
            } else if (rootName.contains("cancelOrderMessage")) {
                logger.info("The message to format is of type:'CancelOrderMessageDto'");
                context = JAXBContext.newInstance(CancelOrderMessageDto.class);
                unmarshaller = context.createUnmarshaller();
                Reader reader = new StringReader(xmlText);
                output = (CancelOrderMessageDto) unmarshaller.unmarshal(reader);
            } else {
                throw new FormatterException("The found type in the XML string does not exist");
            }
        } catch (JAXBException e) {
            throw new FormatterException("Something went wrong while formatting the XML string to a class, a JAXB exception occurred", e);
        }
        return output;
    }
}
