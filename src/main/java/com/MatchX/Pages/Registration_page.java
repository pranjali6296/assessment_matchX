package com.MatchX.Pages;

import org.openqa.selenium.support.CacheLookup;
import com.MatchX.BaseClass.BaseTest;
import com.MatchX.utils.TestUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
public class Registration_page extends BaseTest {
	TestUtils utils = new TestUtils();

//Enter First name 
	@AndroidFindBy(accessibility = "firstNameTextField")
	@CacheLookup
	public MobileElement Firstname;

	public void Enter_First_name(String First_name) {
		clear(Firstname);
		sendKeys(Firstname, First_name, "Enter First name: " + First_name);

	}

	
//Enter Last Name	
	@AndroidFindBy(accessibility = "lastNameTextField")
	@CacheLookup
	public MobileElement Lastname;

	public void Enter_Last_name(String Last_name) {
		clear(Lastname);
		sendKeys(Lastname, Last_name, "Enter Last name: " + Last_name);

	}


//Enter Email_Id	
	@AndroidFindBy(accessibility = "emailTextField")
	@CacheLookup
	public MobileElement Email_Id;

	public void Enter_Email_Id(String EmailId) {
		clear(Email_Id);
		sendKeys(Email_Id, EmailId, "Enter your Email Id: " + EmailId);

	}

	
//Enter phone number 	
	@AndroidFindBy(accessibility = "phoneNumberTextField")
	@CacheLookup
	public MobileElement phone;

	public void Enter_phone(String phone_No) {
		clear(phone);
		sendKeys(phone, phone_No, "Enter a phone Number: " + phone_No);

	}

	
	
//Enter Current Company Name
	@AndroidFindBy(accessibility = "currentCompanyTextField")
	@CacheLookup
	public MobileElement Current_Company_Name;

	public void Enter_Current_Company_Name(String Current_CompanyName) {
		clear(Current_Company_Name);
		sendKeys(Current_Company_Name, Current_CompanyName, "Enter your Current Company Name: " + Current_CompanyName);

	}

	
//Enter city 	
	@AndroidFindBy(accessibility = "cityTextField")
	@CacheLookup
	public MobileElement cityTextField;

	public void Enter_city(String city) {
		clear(cityTextField);
		sendKeys(cityTextField, city, "Enter your current City: " + city);

	}

//Enter country
	@AndroidFindBy(accessibility = "countryTextField")
	@CacheLookup
	public MobileElement countryTextField;

	public void Enter_country(String country) {
		clear(countryTextField);
		sendKeys(countryTextField, country, "Enter your current Country: " + country);

	}

//Enter DOB	
	@AndroidFindBy(accessibility = "dobTextField")
	@CacheLookup
	public MobileElement dobTextField;

	public void Enter_dobTextField(String dobText) {
		clear(dobTextField);
		sendKeys(dobTextField, dobText, "Enter your date of birth: " + dobText);

	}

//click submit Button 
	@AndroidFindBy(xpath = "//android.widget.TextView[@text='submitButton']")
	@CacheLookup
	public MobileElement submitButton;

	public void click_submitButton() {

		click(submitButton, "click on submit Button");
	}

//capture massage Data is in processing.
	@AndroidFindBy(xpath = "//android.widget.TextView[@text='Data is in processing.']")
	@CacheLookup

	public MobileElement Data;

	public String Data_is_in_processing_getErrTxt() {
		String err = getText(Data, "Data is in processing.");
		return err;
	}
	
//capture massage Please enter your current residing Country
	@AndroidFindBy(xpath = "//android.widget.TextView[@text='Please enter your current residing Country']")
	@CacheLookup

	public MobileElement Please_enter_your_current_residing_Country;

	public String Please_enter_your_current_residing_CountrygetErrTxt() {
		String err = getText(Please_enter_your_current_residing_Country, "Please enter your current residing Country");
		return err;
	}

	
	

}
