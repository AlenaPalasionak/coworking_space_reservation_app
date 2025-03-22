package org.example.coworking.dao;

import org.example.coworking.config.DataSourceConfig;
import org.example.coworking.dao.exception.ReservationNotFoundException;
import org.example.coworking.model.Reservation;

import javax.sql.DataSource;
import java.util.List;

public class ReservationDaoFromDbImpl implements ReservationDaoFromDb {
    private final DataSource dataSource;

    public ReservationDaoFromDbImpl() {
        this.dataSource = DataSourceConfig.getDataSource();
    }

    @Override
    public void add(Reservation object) {

    }

    @Override
    public void delete(Reservation object) throws ReservationNotFoundException {

    }

    @Override
    public Reservation getById(Long id) throws ReservationNotFoundException {
        return null;
    }

    @Override
    public List<Reservation> getAll() {
        return null;
    }

    @Override
    public void update(Reservation reservation) {

    }
}
