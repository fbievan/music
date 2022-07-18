package com.zionhuang.music.playback.queues

import com.google.android.exoplayer2.MediaItem

class ListQueue(
    override val title: String?,
    val items: List<MediaItem>,
    val startIndex: Int,
) : Queue {
    override suspend fun getInitialStatus() = Queue.Status(items, startIndex)

    override fun hasNextPage(): Boolean = false

    override suspend fun nextPage() = throw UnsupportedOperationException()
}