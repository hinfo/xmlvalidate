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
public enum Evento {
    ENVIO, CANCELAMENTO, INUTILIZACAO,CONSULTA,NFEAUTORIZACAOLOTE,
    NFERETAUTORIZACAOLOTE,NFEINUTILIZACAO,NFECONSULTANF,
    NFESTATUSSERVICONF,NFERECEPCAOEVENTO,NFEDOWNLOADNF;
    
    
    
    public static Evento getENVIO() {
        return ENVIO;
    }

    public static Evento getCANCELAMENTO() {
        return CANCELAMENTO;
    }

    public static Evento getINUTILIZACAO() {
        return INUTILIZACAO;
    }

    public static Evento getCONSULTA() {
        return CONSULTA;
    }

    public static Evento getNFEAUTORIZACAOLOTE() {
        return NFEAUTORIZACAOLOTE;
    }

    public static Evento getNFERETAUTORIZACAOLOTE() {
        return NFERETAUTORIZACAOLOTE;
    }

    public static Evento getNFEINUTILIZACAO() {
        return NFEINUTILIZACAO;
    }

    public static Evento getNFECONSULTANF() {
        return NFECONSULTANF;
    }

    public static Evento getNFESTATUSSERVICONF() {
        return NFESTATUSSERVICONF;
    }

    public static Evento getNFERECEPCAOEVENTO() {
        return NFERECEPCAOEVENTO;
    }

    public static Evento getNFEDOWNLOADNF() {
        return NFEDOWNLOADNF;
    }

   
    
    
}

