import moment from "moment"
import React from "react"
import DateHelper from "../helpers/DateHelper"
import {expect} from "../testHelpers/importsHelper"

describe("DateHelper", () => {
  describe("getMonthOffsetForQuarter", () => {
    it("returns the number months needed to get first month of quarter", () => {
      const q1Date = moment().set({year: 2019, month: 2, date: 19})
      expect(DateHelper.getMonthOffsetForQuarter(q1Date)).to.eq(2)
    })
  })

  describe("isDateInRange", () => {
    const startDate = moment().startOf("day").set({year: 2020, month: 10, date: 19}).toDate()
    const endDate = moment().startOf("day").set({year: 2020, month: 10, date: 29}).toDate()

    it("Returns false if date is before startDate", () => {
      const dateToCheck = moment().startOf("day").set({year: 2020, month: 10, date: 18}).toDate()
      expect(DateHelper.isDayInDayRange(dateToCheck, startDate, endDate)).to.be.false
    })

    it("Returns true if date equals startDate", () => {
      const dateToCheck = moment().startOf("day").set({year: 2020, month: 10, date: 19}).toDate()
      expect(DateHelper.isDayInDayRange(dateToCheck, startDate, endDate)).to.be.true
    })

    it("Returns true if date equals endDate", () => {
      const dateToCheck = moment().startOf("day").set({year: 2020, month: 10, date: 29}).toDate()
      expect(DateHelper.isDayInDayRange(dateToCheck, startDate, endDate)).to.be.true
    })

    it("Returns false if date is after endDate", () => {
      const dateToCheck = moment().startOf("day").set({year: 2020, month: 10, date: 30}).toDate()
      expect(DateHelper.isDayInDayRange(dateToCheck, startDate, endDate)).to.be.false
    })
  })

  describe("getStartDateOfWeek", () => {
    it("finds monday given wednesday", () => {
      const dateToCheck = moment().set({year: 2019, month: 4, date: 15})
      expect(DateHelper.getStartDateOfWeek(dateToCheck).format("YYYY-MM-DD")).to.equal("2019-05-13")
    })
  })
})
