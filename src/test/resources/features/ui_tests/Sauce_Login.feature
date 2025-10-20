@UI
Feature: Login
  As a user, I want to be able to log in to the SauceDemo application using different credentials.

  @UI1
  Scenario Outline: Verification of login with different user types
    Given I open the browser on the SauceDemo page
    When I enter the credentials "<UserName>" and "<Password>"
    Then I should be redirected to the URL "<urlPath>"

    Examples:
      | UserName                | Password     | urlPath                                  |
      | standard_user           | secret_sauce | https://www.saucedemo.com/inventory.html |
      | locked_out_user         | secret_sauce | https://www.saucedemo.com/               |
      | problem_user            | secret_sauce | https://www.saucedemo.com/inventory.html |
      | performance_glitch_user | secret_sauce | https://www.saucedemo.com/inventory.html |

  @UI2
  Scenario Outline: Verification of inline error when entering invalid credentials
    Given I open the browser on the SauceDemo page
    When I enter the credentials "<UserName>" and "<Password>"
    Then I should see the error message "<errorMsg>"

    Examples:
      | UserName      | Password       | errorMsg                                                                  |
      | invalid_user  | secret_sauce   | Epic sadface: Username and password do not match any user in this service |
      | standard_user | wrong_password | Epic sadface: Username and password do not match any user in this service |
