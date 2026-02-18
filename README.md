# Block Tests

## Projects and data
You can find the 400 fragments that we evaluate [here](data/fragment), time data [here](data/time), coverage data [here](data/coverage), mutation data [here](data/mutation), the block tests we wrote [here](data/test), and our BDK's API [here](appendix.pdf).

## Repository structure
| Directory               | Purpose                                       |
| ------------------------| --------------------------------------------- |
| blocktest               | block-test development kit (BDK)              |
| data                    | raw data (see section above)                  |
| Docker                  | scripts to run our experiments in Docker      |
| extension               | BDK's Maven extension                         |
| scripts                 | our experimental infrastructure               |


## Usage
### Prerequisites:
- A x86-64 architecture machine
- Ubuntu 20.04
- [Docker](https://docs.docker.com/get-docker/)
### Setup
First, you need to build a Docker image. Run the following commands in the terminal.
```sh
docker build -f Docker/Dockerfile . --tag=blocktests
```

### Running Block Tests experiment
```sh
# Enter the following commands outside the Docker container and inside the current repository directory
cd Docker/time

# The command below will run BDK on all projects, results are located at: output/<project_name>-<sha>-<file_name>/output/logs
# You can also find the tests at: output/<project_name>-<sha>-<file_name>/output/logs/<file_name>BlockTest.java
bash run_blocktest_with_time_in_docker.sh projects.txt output

# Similar to the command above, but duplicates tests 10 times
bash run_blocktest_with_time_in_docker.sh -d 10 projects-dup10.txt output-dup10

# Similar to the command above, but duplicates tests 100 times
bash run_blocktest_with_time_in_docker.sh -d 100 projects-dup100.txt output-dup100

# Similar to the command above, but duplicates tests 1000 times
bash run_blocktest_with_time_in_docker.sh -d 1000 projects-dup1000.txt output-dup1000

# The command below runs BDK on all projects and collects coverage
# Coverage results are located at: output-cov/<project_name>-<sha>-<file_name>/output/site
bash run_blocktest_with_time_in_docker.sh -c true projects-coverage.txt output-cov
```
