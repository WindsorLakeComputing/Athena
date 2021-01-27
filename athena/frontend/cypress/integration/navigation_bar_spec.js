import * as Helpers from '../support/commands'

describe("Navigation Bar", () => {
  beforeEach(() => {
    Helpers.resetDB()
  })

  it("has a nav to home page", () => {
    cy.visit(Cypress.env("testUrl"))

    cy.get(".global_header").contains("Maintainer Management")
    cy.get(".global_header").contains("Welcome Hermes Weasley")
    cy.contains("Henry").click()
    cy.get(".global_header").contains("Maintainer Management").click()
    cy.url().should("equal", Cypress.env("testUrl") + "/#/")
  })
})
