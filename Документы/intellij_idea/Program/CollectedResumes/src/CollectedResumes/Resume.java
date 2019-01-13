package CollectedResumes;

class Resume {

    private String id;
    private String jobsName;
    private String url;
    private String workExperience;

    Resume(String id, String jobsName, String url) {
        this.id = id;
        this.jobsName = jobsName;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobsName() {
        return jobsName;
    }

    public void setJobsName(String jobsName) {
        this.jobsName = jobsName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Main.Resume{" +
                "id='" + id + '\'' +
                ", jobsName='" + jobsName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

}
