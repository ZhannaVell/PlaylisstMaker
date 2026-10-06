package com.example.playlisstmaker.player.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlisstmaker.R
import com.example.playlisstmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlisstmaker.player.domain.model.AudioPlayerState
import com.example.playlisstmaker.player.ui.view_model.AudioPlayerViewModel
import com.example.playlisstmaker.search.domain.model.Track
import com.example.playlisstmaker.search.ui.model.TrackParcelable
import com.example.playlisstmaker.utils.Constants
import com.example.playlisstmaker.utils.ImageUrlHelper
import com.example.playlisstmaker.utils.getParcelableExtraCompat

/**
 * Экран воспроизведения трека.
 * Отвечает за
 * Отображение информации о треке
 * Управление воспроизведением через AudioPlayerViewModel (Play/Pause).
 * Отображение прогресса воспроизведения.
 * Паузу при сворачивании приложения.
 * Трек передаётся через Intent как Parcelable (Constants.TRACK_EXTRA).
 * URL для MediaPlayer берётся из track.previewUrl.
 */

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AudioPlayer"
    }
//Доступ к view элементам
    private lateinit var binding: ActivityAudioPlayerBinding
    //Управление MediaPlayer и состоянием
    private lateinit var viewModel: AudioPlayerViewModel

    private var _track: Track? = null
    private val track: Track
        get() = requireNotNull(_track)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
//Восстановление трека из savedInstanceState, потом из Intent
        _track = if (savedInstanceState != null) {
            savedInstanceState.getParcelable<TrackParcelable>(Constants.TRACK_STATE_KEY)?.toTrack()
        } else {
            intent.getParcelableExtraCompat<TrackParcelable>(Constants.TRACK_EXTRA)?.toTrack()
        }

        if (_track == null) {
            throw IllegalArgumentException(Constants.ERROR_TRACK_MISSING)
        }

        val url = track.previewUrl ?: ""
//Создание viewModel через фабрику
        viewModel = ViewModelProvider(this, AudioPlayerViewModel.getFactory(url))
            .get(AudioPlayerViewModel::class.java)

        bindData() //информация о треке отрисовка
        setupListeners() //обработка кнопок
        observeViewModel() //подписка на LiveData

        if (url.isEmpty()) {
            Log.d(TAG, "Track doesn't contain previewUrl")
            binding.btnPlay.isEnabled = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _track?.let {
            outState.putParcelable(Constants.TRACK_STATE_KEY, TrackParcelable.from(it))
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBarInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            view.updatePadding(
                top = statusBarInsets.top,
                bottom = navigationBarInsets.bottom
            )

            insets
        }
    }

    private fun setupListeners() {
        binding.tbAudioPlayer.setNavigationOnClickListener {
            finish()
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
        viewModel.observeState().observe(this) { screenState ->
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

        binding.tvProgressTime.text = getString(R.string.progress_time_format)
        loadCover()
    }

    private fun loadCover() {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen.spacing_s)
        Glide.with(this)
            .load(ImageUrlHelper.getCoverArtwork(track.artworkUrl100))
            .placeholder(R.drawable.ic_placeholder_45)
            .error(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(cornerRadius))
            .into(binding.ivCover)
    }
}