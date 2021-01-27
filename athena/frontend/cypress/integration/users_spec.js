import * as Helpers from "../support/commands"

describe("Mad Hatter Users Page", () => {

  const newUser = {
    firstName: "Johnny",
    lastName: "Appleseed",
    email: "Johnny.Appleseed@users.com",
    amu: "Bolt"
  }

  beforeEach(() => {
    Helpers.resetDB()
  })

  it("creates a user and sees that the new user is added to the bottom of the list", () => {
    cy.visit(Cypress.env("testUrl"))
    cy.get(".dropdown-link").click()
    cy.get("#add-user-button").click()

    cy.url().should("include", "/MadHatterUsers")

    cy.get("h2").contains("Current Users")

    cy.get(".create_user").click()
    cy.get(".error_text").should('have.length', 4)

    cy.get("table thead tr")
      .and("contain", "Last Name")
      .and("contain", "First Name")
      .and("contain", "Email")
      .and("contain", "AMU")

    cy.get("#last_name").type(newUser.lastName)
    cy.get("#first_name").type(newUser.firstName)
    cy.get("#email").type(newUser.email)
    Helpers.selectFromDropdown("#amu-dropdown", "Bolt")
    cy.get(".error_text").should('have.length', 0)
    cy.get(".create_user").click()

    cy.get("table tbody tr:last-child")
      .and("contain", newUser.firstName)
      .and("contain", newUser.lastName)
      .and("contain", newUser.email)
      .and("contain", newUser.amu)
  })

  it("displays an error when adding a duplicate user", () => {
    const dupeUser = {
      firstName: "Johnny",
      lastName: "Appleseed",
      email: "Johnny.Appleseed2@users.com",
      amu: "Bolt"
    }

    cy.visit(Cypress.env("testUrl") + "/#/MadHatterUsers")
    cy.get("#last_name").type(dupeUser.lastName)
    cy.get("#first_name").type(dupeUser.firstName)
    cy.get("#email").type(dupeUser.email)
    Helpers.selectFromDropdown("#amu-dropdown", "Bolt")
    cy.get(".create_user").click()

    cy.get("#last_name").type(dupeUser.lastName)
    cy.get("#first_name").type(dupeUser.firstName)
    cy.get("#email").type(dupeUser.email)
    Helpers.selectFromDropdown("#amu-dropdown", "Bolt")
    cy.get(".create_user").click()

    cy.get("table tbody tr").should('have.length', 1)
    cy.get(".create_error")
  })

  it("has a back button", () => {
    cy.visit(Cypress.env("testUrl") + "/#/MadHatterUsers")
    cy.get(".back-button")
  })
})
