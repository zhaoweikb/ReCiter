package reciter.database.oracle.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import reciter.database.oracle.OracleConnectionFactory;
import reciter.database.oracle.OracleIdentityDao;

@Repository("oracleIdentityDao")
public class OracleIdentityDaoImpl implements OracleIdentityDao {

	private static final Logger slf4jLogger = LoggerFactory.getLogger(OracleIdentityDaoImpl.class);

	@Autowired
	private OracleConnectionFactory oracleConnectionFactory;

	@Override
	public int getBachelorDegreeYear(String cwid) {
		int year = 0;
		Connection connection = oracleConnectionFactory.createConnection();
		if (connection == null) {
			return year;
		}
		ResultSet rs = null;
		PreparedStatement pst = null;
		String sql = "select degree_year from OFA_DB.PERSON p1 "
				+ "inner join "
				+ "(select cwid, degree_year from ofa_db.degree d "
				+ "join OFA_DB.FACORE fac on fac.facore_pk = d.facore_fk "
				+ "join OFA_DB.PERSON p on p.PERSON_PK = fac.FACORE_PK "
				+ "join OFA_DB.INSTITUTE i on i.institute_PK = d.institute_FK "
				+ "left join OFA_DB.DEGREE_NAME n on n.DEGREE_NAME_PK = d.degree_name_fk "
				+ "where p.cwid is not NULL and terminal_degree <> 'Yes' and doctoral_degree is null "
				+ "and md = 'F' and mdphd ='F' and do_degree = 'F' and n.OTHER_PROFESSORIAL = 'F' "
				+ "and degree not like 'M%' and degree not like 'Pharm%' and degree not like 'Sc%' "
				+ "order by degree_year asc) p2 "
				+ "on p2.cwid = p1.cwid and p1.cwid = ?";
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, cwid);
			rs = pst.executeQuery();
			while(rs.next()) {
				year = rs.getInt(1);
			}
		} catch(SQLException e) {
			slf4jLogger.error("Exception occured in query=" + sql, e);
		}
		finally {
			try {
				rs.close();
				pst.close();
				connection.close();;
			} catch(SQLException e) {
				slf4jLogger.error("Unabled to close connection to Oracle DB.", e);
			}
		}
		return year;
	}

	@Override
	public int getDoctoralYear(String cwid) {
		int year = 0;
		Connection connection = oracleConnectionFactory.createConnection();
		if (connection == null) {
			return year;
		}
		ResultSet rs = null;
		PreparedStatement pst = null;
		String sql = "select degree_year from ofa_db.degree d "
				+ "join OFA_DB.FACORE fac on fac.facore_pk = d.facore_fk "
				+ "join OFA_DB.PERSON p ON p.PERSON_PK = fac.FACORE_PK "
				+ "join OFA_DB.INSTITUTE i on i.institute_PK = d.institute_FK "
				+ "left join OFA_DB.DEGREE_NAME n on n.DEGREE_NAME_PK = d.degree_name_fk "
				+ "where p.cwid is not NULL and cwid <> '0' and terminal_degree = 'Yes' and p.cwid = ?";
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, cwid);
			rs = pst.executeQuery();
			while(rs.next()) {
				year = rs.getInt(1);
			}
		} catch(SQLException e) {
			slf4jLogger.error("Exception occured in query=" + sql, e);
		}
		finally {
			try {
				rs.close();
				pst.close();
				connection.close();;
			} catch(SQLException e) {
				slf4jLogger.error("Unabled to close connection to Oracle DB.", e);
			}
		}
		return year;
	}

	@Override
	public List<String> getInstitutions(String cwid) {
		List<String> affiliations = new ArrayList<String>();
		Connection connection = oracleConnectionFactory.createConnection();
		if (connection == null) {
			return affiliations;
		}
		ResultSet rs = null;
		PreparedStatement pst = null;
		String sql = "SELECT cwid, ai.INSTITUTION, 'Academic-PrimaryAffiliation' from OFA_DB.FACORE fac "
				+ "JOIN OFA_DB.AFFIL_INSTITUTE ai ON ai.affil_institute_pk = fac.affil_institute_FK "
                + "JOIN OFA_DB.PERSON p ON p.PERSON_PK = fac.FACORE_PK where cwid is not null and cwid <> '0' and cwid = ?"
                + "union "
                + "SELECT p.cwid, ai.INSTITUTION, 'Academic-AppointingInstitution' FROM OFA_DB.FACORE fac "
                + "JOIN OFA_DB.APPOINTMENT a ON fac.facore_PK = a.facore_fk "
                + "JOIN OFA_DB.AFFIL_INSTITUTE ai ON ai.affil_institute_pk = a.affil_institute_FK "
                + "JOIN OFA_DB.AFFIL_INSTITUTE ai ON ai.affil_institute_pk = fac.affil_institute_FK "
                + "JOIN OFA_DB.PERSON p ON p.PERSON_PK = fac.FACORE_PK "
                + "where cwid is not null and cwid <> '0' and cwid = ?"
                + "union "
                + "SELECT cwid, institution, 'Academic-Degree' from ofa_db.degree d "
                + "join OFA_DB.FACORE fac on fac.facore_pk = d.facore_fk "
                + "join OFA_DB.PERSON p ON p.PERSON_PK = fac.FACORE_PK " 
                + "join OFA_DB.INSTITUTE i on i.institute_PK = d.institute_FK "
                + "left join OFA_DB.DEGREE_NAME n on n.DEGREE_NAME_PK = d.degree_name_fk " 
                + "where cwid is not null and cwid <> '0' and cwid = ?";

		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, cwid);
			pst.setString(2, cwid);
			pst.setString(3, cwid);
			rs = pst.executeQuery();
			while(rs.next()) {
				affiliations.add(rs.getString(2));
			}
		} catch(SQLException e) {
			slf4jLogger.error("Exception occured in query=" + sql, e);
		}
		finally {
			try {
				rs.close();
				pst.close();
				connection.close();;
			} catch(SQLException e) {
				slf4jLogger.error("Unabled to close connection to Oracle DB.", e);
			}
		}
		return affiliations;
	}

	@Override
	public List<String> getPersonalEmailFromOfa(String cwid) {
		List<String> emails = new ArrayList<String>();
		Connection connection = oracleConnectionFactory.createConnection();
		if (connection == null) {
			return emails;
		}
		ResultSet rs = null;
		PreparedStatement pst = null;
		String sql = "select email from "
				+ "(select distinct p.cwid, substr(private_email,1,INSTR(private_email,',')-1) as email "
				+ "FROM OFA_DB.PERSON p where p.private_email like '%,%' "
				+ "UNION "
				+ "select distinct p.cwid, substr(email,1,INSTR(email,',')-1) AS email from OFA_DB.PERSON p "
				+ "where p.email like '%,%' "
				+ "UNION "
				+ "select distinct p.cwid, "
				+ "substr(replace(p.private_email,' ',''),1 + INSTR(replace(p.private_email,' ',''),',')) "
				+ "as email from OFA_DB.PERSON p where p.private_email like '%,%' "
				+ "UNION "
				+ "select distinct p.cwid, "
				+ "substr(replace(p.email,' ',''),1 + INSTR(replace(p.email,' ',''),',')) as email from OFA_DB.PERSON p "
				+ "where p.email like '%,%') where email like '%@%' and email like '%.%' and CWID = ?";
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, cwid);
			rs = pst.executeQuery();
			while(rs.next()) {
				emails.add(rs.getString(1));
			}
		} catch(SQLException e) {
			slf4jLogger.error("Exception occured in query=" + sql, e);
		}
		finally {
			try {
				rs.close();
				pst.close();
				connection.close();;
			} catch(SQLException e) {
				slf4jLogger.error("Unabled to close connection to Oracle DB.", e);
			}
		}
		return emails;
	}

}
