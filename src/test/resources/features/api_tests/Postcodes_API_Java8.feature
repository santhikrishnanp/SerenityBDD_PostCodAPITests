@api @java8
Feature: Postcode Data Processing with Java 8 Streams
  As a Test Engineer
  I want to process postcode data using Java 8 features
  So that I can demonstrate basic knowledge of streams and functional programming

  Background:
    Given the API service is healthy

  @Streams @Filtering
  Scenario: Filter and count postcodes by region using streams
    When I fetch multiple postcodes for analysis:
      | SW1A 1AA |
      | EH1 1YZ  |
      | M1 1AA   |
      | SW1A 2AA |
    Then I should filter postcodes by region "London"
    And the result should contain 2 postcodes

  @Collectors @Grouping
  Scenario: Group postcodes by region using collectors
    When I fetch postcodes across different regions:
      | SW1A 1AA  |
      | SW1A 2AA  |
      | W1A 1AA   |
    Then I should group postcodes by region
    And count postcodes per region
    And the region "London" should have 3 postcodes

  @Optional @NullHandling
  Scenario: Handle invalid postcodes safely with Optional
    When I lookup postcodes with potential missing data:
      | SW1A 1AA |
      | INVALID1 |
      | SW1A 2AA |
    Then I should extract only valid responses using Optional
    And count the valid postcodes as 2
