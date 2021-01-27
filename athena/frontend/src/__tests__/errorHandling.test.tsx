import axios from "axios"
import MockAdaptor from "axios-mock-adapter"
import React from "react"
import RequestHelper from "../helpers/RequestHelper"
import {expect} from "../testHelpers/importsHelper"

describe("request helper error handling", () => {
  let mock: MockAdaptor
  beforeEach(() => {
    mock = new MockAdaptor(axios)
  })
  afterEach(() => {
    mock.reset()
    window.location.hash = "#/"
  })

  it("redirects to /error when there is a non-200 response for post", async () => {
    mock.onPost("/api/maintainers").reply(400)

    const result = await RequestHelper.post("/api/maintainers")
    expect(result).to.be.true
    expect(window.location.hash).to.eq("#/error")
  })

  it("redirects to /error when there is a non-200 response for postJson", async () => {
    mock.onPost("/api/maintainers").reply(500)

    const result = await RequestHelper.postJson("/api/maintainers", "")
    expect(result).to.be.true
    expect(window.location.hash).to.eq("#/error")
  })

  it("redirects to /error when there is a non-200 response for postFile", async () => {
    mock.onPost("/api/maintainers").reply(500)

    const result = await RequestHelper.postFile("/api/maintainers", new FormData())
    expect(result).to.be.true
    expect(window.location.hash).to.eq("#/error")
  })

  it("redirects to /error when there is a non-200 response for delete", async () => {
    mock.onDelete("/api/maintainers").reply(500)

    const result = await RequestHelper.delete("/api/maintainers")
    expect(result).to.be.true
    expect(window.location.hash).to.eq("#/error")
  })

  it("redirects to /error when there is a non-200 response for get", async () => {
    mock.onGet("/api/maintainers").reply(500)

    const {errorStatus} = await RequestHelper.get("/api/maintainers")
    expect(errorStatus).to.be.true
    expect(window.location.hash).to.eq("#/error")
  })

  it("redirects to /error when there is a non-200 response for put", async () => {
    mock.onPut("/api/maintainers").reply(500)

    const result = await RequestHelper.put("/api/maintainers", {})
    expect(result).to.be.true
    expect(window.location.hash).to.eq("#/error")
  })
})
