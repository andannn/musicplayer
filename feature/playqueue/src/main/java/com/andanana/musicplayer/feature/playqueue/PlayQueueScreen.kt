package com.andanana.musicplayer.feature.playqueue

import androidx.compose.runtime.getValue
//
// @Composable
// internal fun PlayQueueScreen(
//    playQueueViewModel: PlayQueueViewModel = hiltViewModel()
// ) {
// //    val queueList by playQueueViewModel.playQueueFlow.collectAsState(initial = emptyList())
//    val playingMedia by playQueueViewModel.playingMediaFlow.collectAsState(null)
//
//    PlayQueueScreenContent(
//        queueList = emptyList(),
//        playingUri = playingMedia?.localConfiguration?.uri
//    )
// }
//
// @OptIn(ExperimentalFoundationApi::class)
// @Composable
// private fun PlayQueueScreenContent(
//    queueList: List<MusicModel>,
//    playingUri: Uri?
// ) {
//    LazyColumn(
//        modifier = Modifier,
//        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
//    ) {
//        item {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(
//                    onClick = { }
//                ) {
//                    Icon(
//                        imageVector = Icons.Rounded.ArrowBack,
//                        contentDescription = "Back"
//                    )
//                }
//                Spacer(modifier = Modifier.width(10.dp))
//                Text(text = "Queue", style = MaterialTheme.typography.titleMedium)
//            }
//        }
//        items(
//            items = queueList,
//            key = { it.contentUri }
//        ) { info ->
//            MusicCard(
//                modifier = Modifier
//                    .padding(vertical = 4.dp)
//                    .animateItemPlacement(),
//                albumArtUri = info.albumUri.toString(),
//                isActive = playingUri == info.contentUri,
//                title = info.title,
//                showTrackNum = false,
//                showSwapIcon = true,
//                artist = info.artist,
//                trackNum = info.cdTrackNumber,
//                date = info.modifiedDate,
//                onMusicItemClick = {
//                },
//                onOptionButtonClick = {
//                }
//            )
//        }
//    }
// }
