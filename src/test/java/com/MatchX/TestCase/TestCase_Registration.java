package com.MatchX.TestCase;
import java.lang.reflect.Method;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.MatchX.BaseClass.BaseTest;
import com.MatchX.Pages.Registration_page;
import com.MatchX.utils.TestUtils;

public class TestCase_Registration extends BaseTest {
	Registration_page Registration;
	
	TestUtils utils = new TestUtils();

	@BeforeClass
	public void beforeClass() throws Exception {	
		closeApp();
		launchApp();
	}

	@AfterClass
	public void afterClass() {
	}

	@BeforeMethod
	public void beforeMethod(Method m) {
		utils.log().info("\n" + "****** starting test:" + m.getName() + "******" + "\n");
	
		
		//create object 
		Registration = new Registration_page();
	}

	@AfterMethod
	public void afterMethod() { 
		
	}
		
		
	@Test(priority = 1,description = "Verify error message 'Please enter your current residing Country' is received when user tries to Submit the Form by leaving 'Country' field empty.")
	
	public void Tc_001() throws Exception {
		
		Registration.Enter_First_name("Ram");
		Registration.Enter_Last_name("jiya");		 
		Registration.Enter_Email_Id("ramJiya@gmail.com");
		Registration.Enter_phone("8237778886");
		Registration.Enter_Current_Company_Name("KK company");
		Registration.Enter_city("nagpur");
		Registration.Enter_country("");
		Registration.Enter_dobTextField("10/02/1993");
		Registration.click_submitButton();
		
		String actualErrTxt =Registration.Please_enter_your_current_residing_CountrygetErrTxt();
		String expectErrTxt = getStrings().get("Please_enter_your_current_residing_Country");
		
		Assert.assertEquals(actualErrTxt, expectErrTxt);
	}		
	
	
	
	@Test(priority = 2,description = "Verify expected message 'Data is in processing.' is received when user types in all fields and press Submit button.")
	
	public void Tc_002() throws Exception {
		
		Registration.Enter_First_name("Ram");
		Registration.Enter_Last_name("jiya");		 
		Registration.Enter_Email_Id("ramJiya@gmail.com");
		Registration.Enter_phone("8237778886");
		Registration.Enter_Current_Company_Name("KK company");
		Registration.Enter_city("nagpur");
		Registration.Enter_country("india");
		Registration.Enter_dobTextField("10/02/1993");	
		Registration.click_submitButton();
		
		String actualErrTxt =Registration.Data_is_in_processing_getErrTxt();
		String expectErrTxt = getStrings().get("Data_is_in_processing");
		
		Assert.assertEquals(actualErrTxt, expectErrTxt);
		
		
	}	
	
}
