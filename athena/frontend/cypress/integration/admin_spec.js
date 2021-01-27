import * as Helpers from '../support/commands'

describe("Admin page", () => {
  beforeEach(() => {
    Helpers.resetDB()
    cy.visit(`${Cypress.env("testUrl")}/#/admin`)
  })

  it("Verify Generate Schedule button is visible", () => {
    cy.get("#minerva-generate-schedule")
  })
})