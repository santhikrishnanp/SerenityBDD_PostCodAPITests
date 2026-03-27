@api
Feature: UK Postcode API Testing Suite Assessment
  As a QA Engineer
  I want to thoroughly test the nearest postcode functionality
  So that I can ensure the accuracy of the API

  Background:
    Given the API service is up & healthy

  @Positive @Nearest
  Scenario Outline: Verify SW1A 1AA is the nearest postcode of its coordinates
    When I search the postcode "<value>"
    Then the api response status code should be 200
    And I extract the longitude and latitude from the response
    When I lookup the nearest postcodes using the extracted coordinates
    Then the nearest postcode returned should be "<value>"
    Examples:
      | value    |
      | SW1A 1AA |

#  @Negative
#  Scenario: Verify the response for an invalid postcode
#    When I search an invalid postcode
#      | value            |
#      |AB1 XX|
#    Then the api response status code should be 404
