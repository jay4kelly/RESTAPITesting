Feature: Validating User Apis
  Test the User REST API's

@AddUser
  Scenario: Verify if a new user can be created
    Given Add new User Payload with "John" "Lennon" "male" "john.lennon1@TheBeatles.com"
    When User calls "UsersResource" with "POST" http request
    Then the api call returns with a status code 201
    And User calls "UsersResource" with "GET" http request for added user
    And verify user created maps to returned User

  @DeleteUser
  Scenario: Delete user just added
    Given  Add Delete "UsersResource" Payload
    When User calls "UsersResource" with "DELETE" http request for added user
    Then the api call returns with a status code 204

  @GetUsers
  Scenario: Verify get users returns a list of users
    Given Add Get Users Payload
    When User calls "UsersResource" with "GET" http request
    Then the api call returns with a status code 200
    And result contains users

