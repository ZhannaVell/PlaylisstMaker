package com.example.playlisstmaker.player.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlisstmaker.R
import com.example.playlisstmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlisstmaker.player.domain.model.AudioPlayerState
import com.example.playlisstmaker.player.ui.view_model.AudioPlayerViewModel
import com.example.playlisstmaker.search.domain.model.Track
import com.example.playlisstmaker.search.ui.model.TrackParcelable
import com.example.playlisstmaker.utils.Constants
import com.example.playlisstmaker.utils.ImageUrlHelper
import com.example.playlisstmaker.utils.getParcelableCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerFragment : Fragment() {

    companion object {
        private const val TAG = "AudioPlayer"
        private const val ARGS_TRACK = "track"

        fun createArgs(track: TrackParcelable): Bundle =
            Bundle().apply {
                putParcelable(ARGS_TRACK, track)
            }
    }


    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!
    private val track: Track by lazy {
        requireArguments().getParcelableCompat<TrackParcelable>(ARGS_TRACK)?.toTrack()
            ?: throw IllegalArgumentException(Constants.ERROR_TRACK_MISSING)
    }
    private val url: String by lazy { track.previewUrl ?: "" }
    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(url)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bindData() //информация о треке отрисовка
        setupListeners() //обработка кнопок
        observeViewModel() //подписка на LiveData

        if (url.isEmpty()) {
            Log.d(TAG, "Track doesn't contain previewUrl")
            binding.btnPlay.isEnabled = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }


    private fun setupListeners() {
        binding.tbAudioPlayer.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnPlay.setOnClickListener {
            viewModel.playPause()
        }
        binding.btnFavorite.setOnClickListener {
            Log.d(TAG, "Favorite clicked")
        }
        binding.btnAddToPlaylist.setOnClickListener {
            Log.d(TAG, "Add to playlist clicked")
        }
    }

    private fun observeViewModel() {
        viewModel.observeState().observe(viewLifecycleOwner) { screenState ->
            updatePlayButton(screenState.playerState)
            binding.btnPlay.isEnabled = screenState.playerState != AudioPlayerState.Default

            binding.tvProgressTime.text = screenState.progressTime
        }
    }


    private fun updatePlayButton(state: AudioPlayerState) {
        val iconRes = when (state) {
            AudioPlayerState.Playing -> R.drawable.ic_pause_100
            AudioPlayerState.Prepared,
            AudioPlayerState.Paused,
            AudioPlayerState.Default -> R.drawable.ic_play_100
        }
        binding.btnPlay.setImageResource(iconRes)
    }

    private fun bindData() {
        val album = track.collectionName
        val year = track.releaseDate?.take(4)

        val trackNameWithAlbum = if (album != null || year != null) {
            val parts = mutableListOf<String>()
            album?.let { parts.add(it) }
            year?.let { parts.add(it) }
            "${track.trackName} (${parts.joinToString(" ")})"
        } else {
            track.trackName
        }
        binding.tvTrackName.text = trackNameWithAlbum
        binding.tvArtistName.text = track.artistName
        binding.tvDurationRight.text = track.trackTime

        if (!track.collectionName.isNullOrEmpty()) {
            binding.tvAlbumRight.text = track.collectionName
            binding.tvAlbumRight.isVisible = true
        } else {
            binding.tvAlbumRight.isVisible = false
        }

        if (!year.isNullOrEmpty()) {
            binding.tvYearRight.text = year
            binding.tvYearRight.isVisible = true
        } else {
            binding.tvYearRight.isVisible = false
        }

        if (!track.primaryGenreName.isNullOrEmpty()) {
            binding.tvGenreRight.text = track.primaryGenreName
            binding.tvGenreRight.isVisible = true
        } else {
            binding.tvGenreRight.isVisible = false
        }

        if (!track.country.isNullOrEmpty()) {
            binding.tvCountryRight.text = track.country
            binding.tvCountryRight.isVisible = true
        } else {
            binding.tvCountryRight.isVisible = false
        }

        binding.tvProgressTime.text = requireContext().getString(R.string.progress_time_format)
        loadCover()
    }

    private fun loadCover() {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen.spacing_s)
        Glide.with(requireContext())
            .load(ImageUrlHelper.getCoverArtwork(track.artworkUrl100))
            .placeholder(R.drawable.ic_placeholder_45)
            .error(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(cornerRadius))
            .into(binding.ivCover)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}