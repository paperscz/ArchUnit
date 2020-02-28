package com.tngtech.archunit.library.dependencies;

import java.util.Collections;

import org.junit.Test;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class EdgeTest {
    private final Object from = new Object();
    private final Object to = new Object();

    @Test
    public void edges_are_equal_iff_from_and_to_are_equal() {

        assertThat(newEdge(from, to)).isEqualTo(newEdge(from, to));
        assertThat(newEdge(from, to)).isNotEqualTo(newEdge(new Object(), to));
        assertThat(newEdge(from, to)).isNotEqualTo(newEdge(from, new Object()));

        Edge<Object, Object> equalWithAttachment = new Edge<>(from, to, singletonList(new Object()));
        assertThat(newEdge(from, to)).isEqualTo(equalWithAttachment);
    }

    @Test
    public void hashCode_of_two_equal_edges_is_equal() {
        Edge<Object, Object> equalEdge = new Edge<>(from, to, singletonList(new Object()));
        assertThat(newEdge(from, to).hashCode()).isEqualTo(equalEdge.hashCode());
    }

    static <NODE, ATTACHMENT> Edge<NODE, ATTACHMENT> newEdge(NODE from, NODE to) {
        return new Edge<>(from, to, Collections.<ATTACHMENT>emptySet());
    }
}
