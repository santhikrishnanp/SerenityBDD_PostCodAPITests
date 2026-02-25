@UI
Feature: Inventory
  As a user, I want to be able to sort the inventory catalogue.

  Background:
    Given I open the browser on the SauceDemo page
    When I enter the credentials "standard_user" and "secret_sauce"

  Scenario: Sorting the catalogue by Price (low to high)
    Given I am on the inventory page
    When I sort the price from low to high
    Then the first item should be "Sauce Labs Onesie" and the price should be "7.99"