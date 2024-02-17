package dev.creator54.seleniumhelper;

import org.testng.annotations.Test;

public class SeleniumHelperTest {
	@Test
    public void test() {
		SeleniumHelper seleniumHelper = SeleniumHelper.getInstance();
		try {
			seleniumHelper.get("https://google.com");
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			SeleniumHelper.quit();
		}
    }
}
