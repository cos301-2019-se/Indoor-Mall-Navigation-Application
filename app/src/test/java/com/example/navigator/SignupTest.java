package com.example.navigator;

import android.widget.Toast;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;



import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SignupTest {
    private Signup testItem;
    private Signup spyItem;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);

        testItem = new Signup();
        spyItem = Mockito.spy(testItem);
    }

    @Test
    public void validateEmail_givenValidEmail_returnsTrue()
    {
        String email = "test@bruteforce.com";
        assertWithMessage("ValidEmail").that(spyItem.validateEmail(email)).isTrue();
    }
    @Test
    public void validateEmail_givenPlainString_returnsFalse()
    {
        doNothing().when(spyItem).toastWrapper(anyString());
        //when(mToast)
        String email = "testingEmail";
        assertWithMessage("@less string").that(spyItem.validateEmail(email)).isFalse();
    }
    @Test
    public void validateEmail_givenInvalidDomain_returnsFalse()
    {
        doNothing().when(spyItem).toastWrapper(anyString());
        String email = "test@bruteforcecom";
        assertWithMessage("Invalid Domain").that(spyItem.validateEmail(email)).isFalse();
    }
    @Test
    public void validatePassword_givenValidPassword_returnsTrue()
    {
        doNothing().when(spyItem).toastWrapper(anyString());
        String password = "Passw0rd";
        assertWithMessage("ValidPassword").that(spyItem.validatePassword(password)).isTrue();
    }
    @Test
    public void validatePassword_givenShortPassword_returnsFalse()
    {
        doNothing().when(spyItem).toastWrapper(anyString());
        String password = "Passw";
        assertWithMessage("ShortPassword").that(spyItem.validatePassword(password)).isFalse();
    }
    @Test
    public void validatePassword_givenNoCapsPassword_returnsFalse()
    {
        doNothing().when(spyItem).toastWrapper(anyString());
        String password = "passw0rd";
        assertWithMessage("NoCapsPassword").that(spyItem.validatePassword(password)).isFalse();
    }
    @Test
    public void validatePassword_givenNoNumPassword_returnsFalse()
    {
        doNothing().when(spyItem).toastWrapper(anyString());
        String password = "Password";
        assertWithMessage("NoNumsPassword").that(spyItem.validatePassword(password)).isFalse();
    }
    @Test
    public void validatePassword_givenNoLowerPassword_returnsFalse()
    {
        doNothing().when(spyItem).toastWrapper(anyString());
        String password = "PASSW0RD";
        assertWithMessage("NoLowerPassword").that(spyItem.validatePassword(password)).isFalse();
    }

}
