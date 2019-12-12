package com.revtekk.nioflex.policy;

public enum ThreadPolicy
{
    /* The thread-pool will not be used to run handleAccept() and handleRead() */
    NO_THREAD_SPAWN,

    /* Only use the thread-pool to run handleAccept() not handleRead() */
    THREAD_FOR_ACCEPT,

    /* Only use the thread-pool to run handleRead() not handleAccept() */
    /* This is the default policy */
    THREAD_FOR_READ,

    /* Use the thread-pool to run handleRead() and handleAccept() */
    THREAD_FOR_ALL
}
