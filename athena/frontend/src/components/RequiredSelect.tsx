import {Component, default as React} from "react"
import Select from "react-select"

interface Props {
  selected: string | undefined,
  onChange: (value: string) => void,
  options: Array<{value: string, label: string}>,
  id: string
}

interface State {
  isValid: boolean
}

class RequiredSelect extends Component<Props, State> {
  constructor(props: any) {
    super(props)
    this.state = {isValid: true}
  }

  public componentDidMount(): void {
    const input: HTMLInputElement | null = document.querySelector(`#${this.props.id} .hidden-input`)
    if (input) {
      input!.addEventListener("invalid", () => {
        input!.classList.add("error")
        this.setState({isValid: false})
      }, false)
    }
  }

  public render() {
    return (
      <div className={"react-select-container"} id={this.props.id}>
        <Select isSearchable={false}
                components={{IndicatorSeparator: () => <></>}}
                onChange={(value) => this.onChange((value as { value: string, label: string }).value)}
                onBlur={this.onBlur}
                value={this.getOptionFromSelection(this.props.selected)}
                classNamePrefix={"react-select"}
                className={`${this.state.isValid ? "" : "react-select-error"}`}
                options={this.props.options}
                placeholder={""}/>
        {this.state.isValid
          ?
            <div className={"error_text_placeholder"}/>
          :
            <div className={"error_text"}>
              please complete
            </div>
        }
        <input className={"hidden-input"}
               required={true}
               value={this.props.selected}
               onChange={() => null}/>
      </div>
    )
  }

  private getOptionFromSelection = (selected: string | undefined) =>
    selected ? this.props.options.find((option) => option.value === selected) : null

  private onBlur = () => {
    if (!this.props.selected) {
      this.setState({isValid: false})
    } else {
      this.setState({isValid: true})
    }
  }

  private onChange = (value: string) => {
    this.props.onChange(value)
    this.setState({isValid: true})
  }
}

export default RequiredSelect
