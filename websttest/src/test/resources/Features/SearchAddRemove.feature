Feature: Basic functionality of the main UI page

  # Used for test assignment
  Scenario Outline: Searching then adding and removing from cart
    Given user navigates to the main Webstaurant page
    When user enters a <keyword> in the search bar
    Then results are returned
    Then user is able to add item to cart
    Then user is able to remove item from cart

    Examples: 
      | keyword              |
      | stainless work table |
