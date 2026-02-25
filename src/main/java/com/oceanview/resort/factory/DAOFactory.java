package com.oceanview.resort.factory;

import com.oceanview.resort.dao.GuestDAO;
import com.oceanview.resort.dao.InvoiceDAO;
import com.oceanview.resort.dao.InvoiceLineItemDAO;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dao.RoomDAO;
import com.oceanview.resort.dao.SystemUserDAO;
import com.oceanview.resort.dao.impl.GuestDAOImpl;
import com.oceanview.resort.dao.impl.InvoiceDAOImpl;
import com.oceanview.resort.dao.impl.InvoiceLineItemDAOImpl;
import com.oceanview.resort.dao.impl.ReservationDAOImpl;
import com.oceanview.resort.dao.impl.RoomDAOImpl;
import com.oceanview.resort.dao.impl.SystemUserDAOImpl;

/*
 * Factory Method style DAO factory.
 * Creates DAO implementations and returns them as interface types.
 */
public class DAOFactory {

    public DAOFactory() {
        // no state for now
    }

    public SystemUserDAO getSystemUserDAO() {
        return new SystemUserDAOImpl();
    }

    public GuestDAO getGuestDAO() {
        return new GuestDAOImpl();
    }

    public RoomDAO getRoomDAO() {
        return new RoomDAOImpl();
    }

    public ReservationDAO getReservationDAO() {
        return new ReservationDAOImpl();
    }

    public InvoiceDAO getInvoiceDAO() {
        return new InvoiceDAOImpl();
    }

    public InvoiceLineItemDAO getInvoiceLineItemDAO() {
        return new InvoiceLineItemDAOImpl();
    }
}