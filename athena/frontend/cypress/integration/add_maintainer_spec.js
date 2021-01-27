import * as Helpers from "../support/commands"

describe("Maintainer Detail Page", () => {
  beforeEach(() => {
    Helpers.resetDB()

    cy.visit(Cypress.env("testUrl"))
    cy.get(".add_maintainer").click()
    cy.contains("Add New Maintainer")
  })

  it("Has a BACK button that returns to Home page", () => {
    cy.get(".back-button").click()

    cy.get(".maintainer-list")
  })

  it("Adds a maintainer after the form is submitted", () => {
    cy.get("#last_name").type("Appleseed")
    cy.get("#first_name").type("Johnny")
    cy.get(".create_maintainer").click()

    cy.visit(Cypress.env("testUrl"))
    cy.get(".maintainer-list").contains("Appleseed, Johnny")
  })
})
