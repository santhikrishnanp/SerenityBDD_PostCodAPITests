@api
Feature: UK Postcode API Testing Suite
  As a QAT Engineer
  I want to thoroughly test the postcode API's functionality and reliability
  So that I can ensure the service meets quality standards

  Background:
    Given the API service is healthy

  @Smoke @DataValidation
  Scenario: Comprehensive validation of single postcode data
    When I lookup the postcode "SW1A 1AA"
    Then the response status code should be 200
    And the response should contain the following fields:
      | field           | value            |
      | postcode       | SW1A 1AA         |
      | country        | England          |
      | region         | London           |
    And all coordinate values should be within UK boundaries

  @Performance @Bulk
  Scenario: Multiple postcode lookup performance test
    When I perform a bulk lookup with the following postcodes:
      | SW1A 1AA |
      | SW1A 2AA |
      | W1A 1AA  |
    Then the response status code should be 200
    And all responses should be received within 2000 milliseconds
    And each postcode result should contain required fields

  @Integration
  Scenario: Cross-validate postcode data with nearest endpoint
    When I lookup the postcode "SW1A 1AA"
    And I search for nearest postcodes using its coordinates
    Then the response status code should be 200
    And the nearest postcodes should be within 1000 meters
    And results should be ordered by distance

  @Validation @Negative
  Scenario Outline: Invalid postcode format handling
    When I lookup the postcode "<postcode>"
    Then the response status code should be <status_code>
    And the error message should contain "<error_text>"

    Examples:
      | postcode | status_code | error_text        |
      | INVALID  | 404         | Invalid postcode  |
      | @@@@     | 404         | Invalid postcode  |
      | 123      | 404         | Invalid postcode  |

  @Boundaries @Regions
  Scenario: Regional boundary and administrative area validation
    When I lookup the postcode "SW1A 1AA"
    Then verify administrative hierarchy:
      | level          | value                               |
      | admin_ward     | St James's                          |
      | admin_district | Westminster                         |
      | region         | London                              |