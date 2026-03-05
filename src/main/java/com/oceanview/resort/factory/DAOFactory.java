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

public final class DAOFactory {

    private DAOFactory() { }

    public static SystemUserDAO getSystemUserDAO() {
        return new SystemUserDAOImpl();
    }

    public static GuestDAO getGuestDAO() {
        return new GuestDAOImpl();
    }

    public static RoomDAO getRoomDAO() {
        return new RoomDAOImpl();
    }

    public static ReservationDAO getReservationDAO() {
        return new ReservationDAOImpl();
    }

    public static InvoiceDAO getInvoiceDAO() {
        return new InvoiceDAOImpl();
    }

    public static InvoiceLineItemDAO getInvoiceLineItemDAO() {
        return new InvoiceLineItemDAOImpl();
    }
}