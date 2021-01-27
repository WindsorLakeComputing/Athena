# Athena README


### Workstation Setup
The goal of this setup is to keep development feeling the same between workstations. If something is missing from a workstation, refer to this document to correct it.

#### Run the [Pivotal Workstation Setup](https://github.com/pivotal/workstation-setup)
- This will install Shiftit, Flycut, Chrome, Iterm, Intelij and others.
    
#### Setup FISH
1. Run `brew install fish`
1. Install ohmyfish with `curl -L https://get.oh-my.fish | fish`
1. Install bobthefish with `omf install bobthefish`. This great for git readability.
1. Clone `git clone https://github.com/powerline/fonts.git --depth=1` and run `./install.sh` inside to install powerline fonts
1. In iterm preferences, navigate to `Profiles` -> `Text`
   1. change font to any powerline font
   1. uncheck Use a different font for non-ASCII text
1. To set fish as the default shell run:
   1. `echo /usr/local/bin/fish | sudo tee -a /etc/shells`
   1. `chsh -s /usr/local/bin/fish`
   
#### Setup Java8 SDK
1. `java -version`
1. If not running Java8 SDK:
    1. `brew tap caskroom/versions`
    1. `brew cask install java8`
    
#### Setup yarn CLI
1. `brew install yarn`
        
#### Add .ssh key for workstation to KR git
1. Use git's ssh keygen steps to create a key for the workstation
    - `https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent`
    - create the key in the default location, `~/.ssh/id_rsa`
1. Add the contents of ~/.ssh/id_rsa.pub to your keys on `https://git.aoc-pathfinder.cloud`
    - add the name of the workstation to the key

#### Clone Project git repos into ~/workspace
1. Clone Athena Project
    - `git clone git@git.aoc-pathfinder.cloud:kessel-run/aoc/athena/athena.git`
1. Clone pipeline project
    - `git clone git@git.aoc-pathfinder.cloud:kessel-run/aoc/athena/pipeline-integration.git`
    
#### Setup pre-push git hook
1. `cd ~/workspace/athena/.git/hooks`
1. `ln -s ../../scripts/pre-push pre-push`

#### Install Crunchy Postgres Docker image
1. Install Docker from `https://store.docker.com/editions/community/docker-ce-desktop-mac`.
1. Follow the installation instructions on the Docker page.
1. From a terminal, run `docker run --name crunchy --restart always -e PG_PRIMARY_PORT=5432 -e PG_ROOT_PASSWORD=secret -e PG_DATABASE=athena -e PG_MODE=primary -e PG_USER=athena -e PG_PASSWORD=password -e PG_PRIMARY_USER=athena_primary -e PG_PRIMARY_PASSWORD=password -d -p 5432:5432 crunchydata/crunchy-postgres:centos7-10.7-2.4.0-rc6`
    * This command pulls the percona-server docker image from docker hub and starts it.
    * The various environment variables set up the initial state of the database
    * The --restart flag ensures that the image starts up when the machine boots.
    * The -p flag forwards port 5432 from the container to port 5432 on the local machine, allowing us to connect to sql at `127.0.0.1:5432`.
    * Docs about the Docker image: `https://crunchydata.github.io/crunchy-containers/stable/container-specifications/crunchy-postgres/`    
1. Run `brew install pgcli` to install the PostgreSQL CLI tool and/or go to `https://dbeaver.io/download/` to install DBeaver, a GUI tool.
1. Run `pgcli -h 127.0.0.1 -p 5432 athena -uathena -W` to verify connection to database. Use `password` when prompted for the password.

#### Managing your db with flyway
1. download flyway with homebrew
1. configure it following instructions here https://flywaydb.org/getstarted/firststeps/commandline  your flyway.conf is probably located
somewhere like `/usr/local/Cellar/flyway/5.2.4/libexec/conf/flyway.conf`   your url is probably something like `jdbc:postgresql://localhost:5432/athena`

#### Install Fly, the CLI for Concourse and configure the AOC target
1. [Fly Download Location for current Kessel Run fly](https://ci.aoc-pathfinder.cloud/api/v1/cli?arch=amd64&platform=darwin)
1. Execute `install fly /usr/local/bin` to enable fly CLI
1. Create aoc target:
    - `fly --target aoc login --team-name athena --concourse-url https://ci.aoc-pathfinder.cloud`
        
#### Intellij Configuration

##### Plugins 
- Rainbowbrackets

### Useful Commands

#### Fly
##### Push updated CI configuration
- `fly -t aoc set-pipeline -c ~/workspace/pipeline-integration/pipeline.yml -p athena --load-vars-from params.yml`
##### Intercept recently run CI job
- `fly -t aoc intercept -j athena/job-name -b build-number`

#### Gradle backend commands
##### Build Athena project
- From Athena project directory run `./gradle build`
##### Run Athena project
- From Athena project directory run `./gradle bootRun`
- The frontend can be reached statically from `localhost:8080`

#### Yarn
##### Install node packages and build frontend
- from <project directory>/frontend execute `yarn build`
##### Start reloading frontend
- from <project directory>/frontend execute `yarn start`

#### Cloud Foundry (PCF)
##### Deploying to athena-dev on devstar
- From a terminal, run: `cf login --sso -a api.sys.devstar.aoc-pathfinder.cloud`
- Follow instructions to authenticate with PCF
- `./gradlew build` to create the .jar for deployment
- `cf push -f cf/manifest.yml --vars-file cf/variables/develop.yml -p build/libs/Athena-0.0.1-SNAPSHOT.jar`
