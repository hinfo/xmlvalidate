/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author icone
 */
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ValidacaoXML implements ErrorHandler {

    private List<String> listaComErrosDeValidacao;

    public ValidacaoXML() {
        this.listaComErrosDeValidacao = new ArrayList<String>();
    }
    public static String validarString(String xml, Evento event) {

        String resultado = "";

        try {
            /**
             * XSD usado para Validar o XML. Use o XSD adequado para cada XML a
             * ser validado: enviNFe_v2.00.xsd (Envio do Lote) inutNFe_v2.00.xsd
             * (Inutilização de Número da NF-e) cancNFe_v2.00.xsd (Cancelamento
             * da NF-e) ...
             */
            String xsd = "";

            switch (event){
                case ENVIO:
                    xsd = "src/schemas/enviNFe_v3.10.xsd";
                    break;
                case CANCELAMENTO:
                    xsd = "src/schemas/inutNFe_v3.10.xsd";
                    break;
                case CONSULTA:
                    xsd = "src/schemas/consSitNFe_v3.10.xsd";
                    break;
                case NFECONSULTANF:
                    xsd = "src/schemas/consSitNFe_v3.10.xsd";
                    break;
                default:
                    System.out.println("Evento desconhecido.");
                    System.exit(0);
            }

            ValidacaoXML validacaoXML = new ValidacaoXML();
            List<String> errosValidacao = validacaoXML.validateXml(normalizeXML(xml.toString()), xsd);
            if ((errosValidacao != null) && (errosValidacao.size() > 0)) {
                StringBuilder errors = new StringBuilder();
                for (String msgError : errosValidacao) {
                    info("| Erro XML: " + msgError);
                    errors.append(msgError);
                    errors.append("\n");
                }
                resultado = "Arquvo contém erros:\n" 
                        + errors;
            } else {
                info("| OK: XML Validado com Sucesso!");
                resultado = "Arquivo validado com Sucesso.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public static String validar(String arqXml, Evento event) {

        String resultado = "";

        try {
            /**
             * XSD usado para Validar o XML. Use o XSD adequado para cada XML a
             * ser validado: enviNFe_v2.00.xsd (Envio do Lote) inutNFe_v2.00.xsd
             * (Inutilização de Número da NF-e) cancNFe_v2.00.xsd (Cancelamento
             * da NF-e) ...
             */
            String xsd = "";

            switch (event){
                case ENVIO:
                    xsd = "src/schemas/enviNFe_v3.10.xsd";
                    break;
                case CANCELAMENTO:
                    xsd = "src/schemas/inutNFe_v3.10.xsd";
                    break;
                case CONSULTA:
                    xsd = "src/schemas/consSitNFe_v3.10.xsd";
                    break;
                case NFECONSULTANF:
                    xsd = "src/schemas/consSitNFe_v3.10.xsd";
                    break;
                default:
                    System.out.println("Evento desconhecido.");
                    System.exit(0);
            }

            /**
             * Caminho do Arquivo XML a ser validado.
             */
            String caminhoArquivo = arqXml;//"C:/JavaC/NF-e/env-lot.xml";  

            StringBuilder xml = new StringBuilder();
            String linha = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(caminhoArquivo), "UTF-8")); //ou ISO-8859-1 
            while ((linha = in.readLine()) != null) {
                xml.append(linha);
            }
            in.close();

            ValidacaoXML validacaoXML = new ValidacaoXML();
            List<String> errosValidacao = validacaoXML.validateXml(normalizeXML(xml.toString()), xsd);
            
            
            if ((errosValidacao != null) && (errosValidacao.size() > 0)) {
                String errors = null;
                for (String msgError : errosValidacao) {
                    info("| Erro XML: " + msgError);
                    errors = msgError;
                }
                resultado = "Arquvo contém erros.\n"
                        + errors;
            } else {
                info("| OK: XML Validado com Sucesso!");
                resultado = "Arquivo validado com Sucesso.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public static String normalizeXML(String xml) {
        if ((xml != null) && (!"".equals(xml))) {
            xml = xml.replaceAll("\\r\\n", "");
            xml = xml.replaceAll(" standalone=\"no\"", "");
        }
        return xml;
    }

    public List<String> validateXml(String xml, String xsd) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", xsd);
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(this);
        } catch (ParserConfigurationException ex) {
            error("| validateXml():");
            throw new Exception(ex.toString());
        }

        org.w3c.dom.Document document;
        try {
            document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
            org.w3c.dom.Node rootNode = document.getFirstChild();
            info("| Validando Node: " + rootNode.getNodeName());
            return this.getListaComErrosDeValidacao();
        } catch (Exception ex) {
            error("| validateXml():");
            throw new Exception(ex.toString());
        }
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        if (isError(exception)) {
            listaComErrosDeValidacao.add(tratamentoRetorno(exception.getMessage()));
        }
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        listaComErrosDeValidacao.add(tratamentoRetorno(exception.getMessage()));
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        listaComErrosDeValidacao.add(tratamentoRetorno(exception.getMessage()));
    }

    private String tratamentoRetorno(String message) {
        message = message.replaceAll("cvc-type.3.1.3:", "");
        message = message.replaceAll("cvc-complex-type.2.4.a:", "");
        message = message.replaceAll("cvc-complex-type.2.4.b:", "");
        message = message.replaceAll("The value", "O valor");
        message = message.replaceAll("of element", "do campo");
        message = message.replaceAll("is not valid", "não é valido");
        message = message.replaceAll("Invalid content was found starting with element", "Encontrado o campo");
        message = message.replaceAll("One of", "Campo(s)");
        message = message.replaceAll("is expected", "é obrigatorio");
        message = message.replaceAll("Cannot find the declaration", "Não foi possível achar a declaração");
        message = message.replaceAll("\\{", "");
        message = message.replaceAll("\\}", "");
        message = message.replaceAll("\"", "");
        message = message.replaceAll("http://www.portalfiscal.inf.br/nfe:", "");
        return message.trim();
    }

    public List<String> getListaComErrosDeValidacao() {
        return listaComErrosDeValidacao;
    }

    private boolean isError(SAXParseException exception) {
        if (exception.getMessage().startsWith("cvc-pattern-valid")
                || exception.getMessage().startsWith("cvc-maxLength-valid")
                || exception.getMessage().startsWith("cvc-datatype")) {
            return false;
        }
        return true;
    }

    private static void error(String error) {
        System.out.println("ERROR: " + error);
    }

    private static void info(String info) {
        System.out.println("INFO: " + info);
    }

}
