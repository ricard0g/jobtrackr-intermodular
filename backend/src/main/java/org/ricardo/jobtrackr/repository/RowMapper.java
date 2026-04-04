package org.ricardo.jobtrackr.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class RowMapper<T> {
    protected abstract T mapRow(ResultSet rs) throws SQLException;
}
