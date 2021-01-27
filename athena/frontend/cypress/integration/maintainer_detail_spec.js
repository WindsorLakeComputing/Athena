import * as Helpers from "../support/commands"
import moment from "moment"

describe("Maintainer Detail Page", () => {
  beforeEach(() => {
    Helpers.resetDB()

    cy.visit(Cypress.env("testUrl"))
  })

  it("Visits the page for a specific maintainer", () => {
    cy.contains("Henry").click()
    cy.url().should("include", "/maintainer/")
    cy.get("h1").and("contain", "Henry").and("contain", "Arnold").and("contain", "TSgt")
    cy.get(".maintainer-attributes").contains("D").and("contain", "S").and("contain", "M")
  })

  it("has a back button to the schedule view to the week navigated from", () => {
    cy.get("[aria-label=\"Tuesday, May 7, 2019\"]").click()
    cy.contains("Henry").click()
    cy.url().should("include", "/maintainer/")
    cy.get(".back-button").click()
    cy.url().should("equal", Cypress.env("testUrl") + "/#/")
    cy.get("[aria-label=\"Selected. Tuesday, May 7, 2019\"]")
  })

  it("Sees all of a maintainers absences", () => {
    cy.contains("Henry").click()
    cy.get(".absence-list-title").contains("Upcoming Absences")
    cy.get(".table-list").get(".absence").should("have.length", 1)

    cy.get(".table-list .absence").first().as("absenceRow")
    cy.get("@absenceRow")
      .children()
      .eq(0)
      .contains("05/14/2019")
    cy.get("@absenceRow")
      .children()
      .eq(1)
      .contains("05/14/2019")
    cy.get("@absenceRow")
      .contains("Rodeo")
    cy.get("@absenceRow")
      .children()
      .eq(5)
      .contains("Delete Absence")
  })

  const startDate = moment().add(4, "day")
  const endDate =  moment().add(7, "day")
  const fullDateFormat = "dddd, MMMM D, YYYY"
  const shortDateFormat = "MM/DD/YYYY"

  const startDateFullFormat = startDate.format(fullDateFormat)
  const startDateShortFormat = startDate.format(shortDateFormat)

  const endDateFullFormat = endDate.format(fullDateFormat)
  const endDateShortFormat = endDate.format(shortDateFormat)

  it("Can add an absence to a Maintainer", () => {
    cy.contains("Henry").click()
    cy.get("#start-date-id").click()
    cy.get("[aria-label=\"Calendar\"]").should("exist")

    cy.get(`[aria-label=\"${startDateFullFormat}\"]`).click()
    cy.get(`[aria-label=\"${endDateFullFormat}\"]`).click()

    cy.get("#start-date-id").should("have.value", startDateShortFormat)
    cy.get("#end-date-id").should("have.value", endDateShortFormat)


    cy.get("#absence_description").type("Measles")
    cy.get(".create_absence").contains("Create")
    cy.get(".create_absence").click()

    //same dates again, different format
    cy.get(".table-list").contains(startDateShortFormat)
    cy.get(".table-list").contains(endDateShortFormat)
    cy.get(".table-list").contains("Measles")
    cy.get(".app-name").click({force: true})

    const someDateInTheAbsence = startDate.add(1, "day").format(fullDateFormat)
    cy.get(`[aria-label=\"${someDateInTheAbsence}\"]`).first().click()
    cy.get("tr.maintainer").contains("Measles")
  })

  it("Can add a single day absence to a Maintainer", () => {
    cy.contains("Henry").click()
    cy.get("#start-date-id").click()
    cy.get("[aria-label=\"Calendar\"]").should("exist")

    //Have to to do it twice for the current calendar picker, once for start, once for end
    cy.get(`[aria-label=\"${startDateFullFormat}\"]`).click()
    cy.get(`[aria-label=\"${startDateFullFormat}\"]`).click()

    cy.get("#start-date-id").should("have.value", startDateShortFormat)
    cy.get("#end-date-id").should("have.value", startDateShortFormat)

    cy.get("#absence_description").type("PartyPartyPartyPartyParty")
    cy.get("#absence_hours").type("2100-2400")
    cy.get("#absence_location").type("Vegas")
    cy.get(".create_absence").contains("Create")
    cy.get(".create_absence").click()
    cy.get(".table-list").contains(startDateShortFormat)
    cy.get(".table-list").contains(startDateShortFormat)
    cy.get(".table-list").contains("2100-2400")
    cy.get(".table-list").contains("PartyPartyPartyParty")
    cy.get(".table-list").contains("Vegas")

    cy.get("#start-date-id").should("not.have.value", startDateShortFormat)
    cy.get("#end-date-id").should("not.have.value", startDateShortFormat)
    cy.get("#absence_description").should("not.have.value", "PartyPartyPartyPartyParty")
    cy.get("#absence_hours").should("not.have.value", "2100-2400")
    cy.get("#absence_location").should("not.have.value", "Vegas")
  })

  it("Can delete absences", () => {
    cy.contains("Henry").click()

    cy.get(".table-list .absence").eq(0).as("absenceRow")
    cy.get("@absenceRow")
      .contains("Rodeo")
    cy.get(".table-list").get(".absence").should("have.length", 1)

    cy.get(".table-list .absence").eq(0).as("rodeoRow")
    cy.get("@rodeoRow")
      .children()
      .eq(5)
      .click()

    cy.get(".table-list").should("not.exist")
  })

  it("Can set the Red X/Limited Red X cert on a maintainer", () => {
    cy.get(".table-list .maintainer td")
      .first().as("maintainerName")
    cy.get("@maintainerName").click()
    cy.get("label[for=red-x-radio]").contains("Red X")
    cy.get("label[for=limited-red-x-radio]").contains("Limited Red X")
    cy.get("#red-x-radio").click()
    cy.get(".maintainer-attributes").contains("X")
    cy.get("#limited-red-x-radio").click()
    cy.get(".maintainer-attributes").contains("L/X")
    cy.get("#no-red-x-radio").click() // Undo selection for next test run
  })

  it("Can set and remove Hot Pits cert on a maintainer", () => {
    cy.get(".table-list .maintainer td")
      .first().as("maintainerName")
    cy.get("@maintainerName").click()

    cy.get(".certifications_tab").click()

    cy.get(".certification_checkbox input").eq(2).check()
    cy.get(".maintainer-attributes").contains("HP")

    cy.get(".certification_checkbox input").eq(2).uncheck()
    cy.get(".maintainer-attributes").should("not.contain", "HP")
  })
})
