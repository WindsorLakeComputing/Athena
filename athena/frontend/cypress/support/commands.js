const CERTS = 7
const CERT_OFFSET = 4

export const validateCertHeaders = (expectedHeadings) => {
  cy.get(".table-list table thead").as("tableHeader")

  cy.get("@tableHeader")
    .children()
    .eq(0).children()
    .as("columnHeadings")

  for (let i = 0; i < expectedHeadings.length; i++) {
    cy.get("@columnHeadings")
      .eq(i + CERT_OFFSET)
      .contains(expectedHeadings[i])
  }
}

export const getWeeklyAvailability = (index, dayOfWeek) => {
  cy.get(".table-list tbody").children().as("maintainerRows")

  return cy.get("@maintainerRows")
    .eq(index)
    .children()
    .eq(CERT_OFFSET + CERTS + dayOfWeek)
}

export const getMaintainerCertificateField = (index, certIndex) => {

  cy.get(".table-list tbody").first()
    .children().eq(index).children().as("maintainerRow")

  return cy.get("@maintainerRow")
    .eq(CERT_OFFSET + certIndex)
}

export const resetDB = () => {
  cy.exec('../scripts/ResetDatabaseAndAdd.sh ./cypress/sql/e2e.sql')
}

export const selectFromDropdown = (dropdownSelector, option) => {
  cy.get(`${dropdownSelector} .react-select__indicator`).trigger("mousedown")
  cy.get(`${dropdownSelector} .react-select__option`).contains(option).click()
}
