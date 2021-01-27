import moment from "moment"
import * as React from "react"
import {Component} from "react"
import {HashRouter, Route, RouteComponentProps} from "react-router-dom"
import AddMaintainerPage from "./components/addMaintainer/AddMaintainerPage"
import MadHatterUsersView from "./components/MadHatterUsers/MadHatterUsersView"
import MaintainerDetail from "./components/maintainerDetail/MaintainerDetail"
import NavigationBar from "./components/NavigationBar"
import GenerateGroup from "./components/scheduleView/GenerateGroup"
import ScheduleView from "./components/scheduleView/ScheduleView"
import RequestHelper from "./helpers/RequestHelper"
import {Maintainer} from "./models/Maintainer"
import erroricon from "./resources/images/erroricon.png"
import withTheme from "./withTheme"

interface State {
  maintainers: Maintainer[],
  userName: string | null,
  date: moment.Moment
}

class App extends Component<{}, State> {

  constructor(props: any) {
    super(props)
    this.state = {
      maintainers: Array<Maintainer>(),
      userName: null,
      date: moment()
    }
  }

  public async componentWillMount() {
    await this.getMaintainerList()
    await this.getLoggedInUser()
  }

  public getMaintainerList = async () => {
    RequestHelper.get("/api/maintainers").then(({errorStatus, body}) => {
      if (!errorStatus) {
        const maintainers = body.maintainers.map((maintainer: any) => Maintainer.fromApiResponse(maintainer))
        this.setState({maintainers})
      }
    })
  }

  public getLoggedInUser = async () => {
    const {errorStatus, body} = await RequestHelper.get("/api/user")
    if (!errorStatus) {
      this.setState({userName: body.given_name})
    }
  }

  public render() {
    const {maintainers, userName} = this.state

    return (
      <div className="App">
        <HashRouter>
          <>
            <NavigationBar
              userName={userName}
              getMaintainerList={this.getMaintainerList}
            />
            <Route
              exact
              path={"/MadHatterUsers"}
              render={() => (
                <MadHatterUsersView />
              )}
            />
            <Route
              exact
              path={"/admin"}
              render={() => (
                <GenerateGroup/>
              )}
            />
            <Route
              exact
              path="/maintainer/:maintainerId"
              render={(props: RouteComponentProps<{ maintainerId: string }>) => {
                if (props.match.params.maintainerId === "new") {
                  return <AddMaintainerPage getMaintainerList={this.getMaintainerList}/>
                }

                const maintainer = maintainers.find((element) => {
                  return element.id.toString() === props.match.params.maintainerId
                })

                if (maintainer !== undefined) {
                  return (<MaintainerDetail maintainer={maintainer} reloadMaintainer={this.reloadMaintainer}/>)
                } else {
                  return (<h1>Maintainer not found!</h1>)
                }
              }}
            />
            <Route
              exact
              path="/error"
              render={() => (
                <div className={"error-message"}>
                  <img src={erroricon} alt="¯\_(ツ)_/¯"/>
                  <div>There Was An Error!</div>
                </div>
              )}
            />
            <Route
              exact
              path="/"
              render={() => (
                <ScheduleView
                  maintainers={this.state.maintainers}
                  reloadMaintainer={this.reloadMaintainer}
                  date={this.state.date}
                  handleDateChange={this.handleDateChange}/>
              )}
            />
          </>
        </HashRouter>
      </div>
    )
  }

  private reloadMaintainer = async (maintainerId: number) => {
    const {errorStatus, body} = await RequestHelper.get(`/api/maintainers/${maintainerId}`)
    if (!errorStatus) {
      const reloadedMaintainer = Maintainer.fromApiResponse(body)
      this.setState({
        maintainers: this.state.maintainers.map((maintainer) =>
          maintainer.id === maintainerId ? reloadedMaintainer : maintainer)
      })
    }
  }

  private handleDateChange = (date: moment.Moment | null) => date && this.setState({date})
}

export default withTheme(App)
