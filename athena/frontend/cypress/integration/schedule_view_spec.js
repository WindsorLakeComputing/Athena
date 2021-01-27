import * as Helpers from "../support/commands"

describe("Schedule View", () => {
  beforeEach(() => {
    Helpers.resetDB()
    cy.visit(Cypress.env("testUrl"))
  })

  it("Displays a calendar", () => {
    cy.get(":nth-child(2) > .CalendarMonth > .CalendarMonth_caption > strong")
      .first().contains("April")
    cy.get(":nth-child(3) > .CalendarMonth > .CalendarMonth_caption > strong")
      .first().contains("May")
    cy.get(":nth-child(4) > .CalendarMonth > .CalendarMonth_caption > strong")
      .first().contains("June")
  })

  it("Sees columns for maintainers certificates on the maintainer list", () => {
    Helpers.validateCertHeaders(["X", "L/X", "I/E", "Run", "HP", "TS", "Bore"])

    Helpers.getMaintainerCertificateField(0, 0).within(() => {
      cy.get("img").should("exist")
    })
    Helpers.getMaintainerCertificateField(0, 3).within(() => {
      cy.get("img").should("exist")
    })
    Helpers.getMaintainerCertificateField(0, 6).within(() => {
      cy.get("img").should("exist")
    })
  })

  it("Shows absences in the maintainer list for a specific week", () => {
    cy.get("[aria-label=\"Tuesday, May 14, 2019\"]").click()
    Helpers.getWeeklyAvailability(0, 1).contains("Rodeo")
  })

  it("Navigates to a maintainer detail after clicking a maintainer in the table", () => {
    cy.get(".maintainer").first().click()

    cy.get(".create_absence_form").should("exist")
  })

  it("Add and remove standbys for a maintainer", () => {
    cy.get(".maintainer .standby-add-button").first().contains("Add Standby")
    cy.get(".maintainer .standby-add-button").first().click()
    cy.get(".maintainer .standby-delete-button").first().contains("Standby")
    cy.get(".maintainer .standby-delete-button").first().click()
    cy.get(".maintainer .standby-add-button").first().contains("Add Standby")
  })
})
