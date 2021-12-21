package github.cli.app.commit;

import github.cli.app.req.Mediator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("Domain")
public class CommitTest {

    @Test
    public void successfulCommitRequestShouldReturnId() throws Exception{
        String fakeCommitId = "123";

        Commit commit = new Mediator(null).reqCommit(fakeCommitId);

        assertThat(commit.id())
                .isEqualTo(fakeCommitId);
    }

}
