package test.tmp;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

import java.net.InetAddress;
import java.util.Collection;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Test HibernateAuditLogger.
 */
public class HibernateAuditLoggerTest {

  @Factory(dataProvider = "databases", dataProviderClass = A.class)
  public HibernateAuditLoggerTest(String databaseType, final String databaseVersion) {
  }

  @BeforeMethod
  public void setUp() throws Exception {
  }

  @AfterMethod
  public void tearDown() throws Exception {
  }

  public Class<?>[] getHibernateMappingClasses() {
    return new Class[] { null};
  }  

  @Test
  public void testLogging() throws Exception {
//    HibernateAuditLogger logger = new HibernateAuditLogger(5, 1);
//    logger.setSessionFactory(getSessionFactory());
//    
//    Collection<AuditLogEntry> logEntries = logger.findAll();
//    assertEquals(0, logEntries.size()); 
//    
//    logger.log("jake", "/Portfolio/XYZ123", "View", true);
//    logger.log("jake", "/Portfolio/XYZ345", "Modify", "User has no Modify permission on this portfolio", false);
//    
//    logger.flushCache();
//    logger.flushCache();
//    
//    logEntries = logger.findAll();
//    assertEquals(2, logEntries.size()); 
//    
//    for (AuditLogEntry entry : logEntries) {
//      assertEquals("jake", entry.getUser());
//      assertEquals(InetAddress.getLocalHost().getHostName(), entry.getOriginatingSystem());
//
//      if (entry.getObject().equals("/Portfolio/XYZ123")) {
//        assertEquals("View", entry.getOperation());
//        assertNull(entry.getDescription());
//        assertTrue(entry.isSuccess());
//      
//      } else if (entry.getObject().equals("/Portfolio/XYZ345")) {
//        assertEquals("Modify", entry.getOperation());
//        assertEquals("User has no Modify permission on this portfolio", entry.getDescription());
//        assertFalse(entry.isSuccess());
//      
//      } else {
//        fail("Unexpected object ID");
//      }
//    }
  }

}
