package com.example.playlisstmaker.presentation.ui.audioplayer

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlisstmaker.domain.api.AudioPlayerInteractor
import com.example.playlisstmaker.domain.models.AudioPlayerState
import com.example.playlisstmaker.utils.Constants
import com.example.playlisstmaker.R
import com.example.playlisstmaker.di.Creator
import com.example.playlisstmaker.domain.models.Track
import com.example.playlisstmaker.utils.ImageUrlHelper
import com.example.playlisstmaker.utils.getParcelableExtraCompat
import com.google.android.material.appbar.MaterialToolbar

class AudioPlayerActivity : AppCompatActivity() {
    companion object {


        private const val TAG = "AudioPlayer"
    }


    private var _track: Track? = null
    private val track: Track
        get() = requireNotNull(_track)

    private lateinit var interactor: AudioPlayerInteractor


    private var isPlaying = false
    private var isFavorite = false

    //VIEWS

    private lateinit var tbAudioPlayer: MaterialToolbar
    private lateinit var ivCover: ImageView
    private lateinit var tvTrackName: TextView
    private lateinit var tvArtistName: TextView

    //Заголовки
    private lateinit var tvDurationLeft: TextView
    private lateinit var tvAlbumLeft: TextView
    private lateinit var tvYearLeft: TextView
    private lateinit var tvGenreLeft: TextView
    private lateinit var tvCountryLeft: TextView

    //Значения
    private lateinit var tvDurationRight: TextView
    private lateinit var tvAlbumRight: TextView
    private lateinit var tvYearRight: TextView
    private lateinit var tvGenreRight: TextView
    private lateinit var tvCountryRight: TextView

    //Кнопки
    private lateinit var btnAddToPlaylist: ImageButton
    private lateinit var btnPlay: ImageButton
    private lateinit var btnFavorite: ImageButton

    private lateinit var tvProgressTime: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_audio_player)

        setupWindowInsets()

        _track = if (savedInstanceState != null) {
            savedInstanceState.getParcelable(Constants.TRACK_STATE_KEY)
        } else {
            intent.getParcelableExtraCompat(Constants.TRACK_EXTRA)
        }

        if (_track == null) {
            throw IllegalArgumentException(Constants.ERROR_TRACK_MISSING)
        }

        interactor = Creator.provideAudioPlayerInteractor(
            onPrepared = ::onPlayerPrepared,
            onCompletion = ::onPlayerCompletion,
            onError = ::onPlayerError,
            onStateChanged = ::onPlayerStateChanged,
            onTimeUpdate = ::onTimeUpdate
        )

        initViews()
        bindData()
        setupListeners()
        preparePlayer()


    }
    private fun onPlayerPrepared() {
        btnPlay.isEnabled = true
        updatePlayButton()
    }

    private fun onPlayerCompletion() {
        updatePlayButton()
        tvProgressTime.text = getString(R.string.progress_time_format)
    }

    private fun onPlayerError() {
        btnPlay.isEnabled = false
        updatePlayButton()
    }

    private fun onPlayerStateChanged(state: Int) {
        updatePlayButton()
    }

    private fun onTimeUpdate(time: String) {
        tvProgressTime.text = time
    }

    override fun onPause() {
        super.onPause()
        if (interactor.getState() == AudioPlayerState.PLAYING) {
            interactor.pause()

        }
    }

    override fun onDestroy() {
        interactor.release()

        super.onDestroy()

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _track?.let {
            outState.putParcelable(Constants.TRACK_STATE_KEY, it)
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.root)
        ) { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navigationBarInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            view.updatePadding(
                top = statusBarInsets.top,
                bottom = navigationBarInsets.bottom
            )

            insets
        }
    }

    private fun preparePlayer() {
        val previewUrl = track.previewUrl
        if (previewUrl.isNullOrEmpty()) {
            Log.d(TAG, "Track doesn't contain previewUrl")
            btnPlay.isEnabled = false
            return
        }
        interactor.prepare(previewUrl)
    }


    private fun playbackControl() {
        when (interactor.getState()) {
            AudioPlayerState.PREPARED, AudioPlayerState.PAUSED -> {
                interactor.start()

            }

            AudioPlayerState.PLAYING -> {
                interactor.pause()

            }

            AudioPlayerState.DEFAULT -> {
                Log.d(TAG, "Player isn't ready")
            }

        }
    }

    private fun updatePlayButton() {
        when (interactor.getState()) {
            AudioPlayerState.PLAYING -> {
                btnPlay.setImageResource(R.drawable.ic_pause_100)
            }

            AudioPlayerState.PREPARED, AudioPlayerState.PAUSED, AudioPlayerState.DEFAULT -> {
                btnPlay.setImageResource(R.drawable.ic_play_100)
            }
        }
    }


    private fun initViews() {
        tbAudioPlayer = findViewById(R.id.tbAudioPlayer)
        ivCover = findViewById(R.id.ivCover)
        tvTrackName = findViewById(R.id.tvTrackName)
        tvArtistName = findViewById(R.id.tvArtistName)

        tvDurationLeft = findViewById(R.id.tvDurationLeft)
        tvAlbumLeft = findViewById(R.id.tvAlbumLeft)
        tvYearLeft = findViewById(R.id.tvYearLeft)
        tvGenreLeft = findViewById(R.id.tvGenreLeft)
        tvCountryLeft = findViewById(R.id.tvCountryLeft)

        tvDurationRight = findViewById(R.id.tvDurationRight)
        tvAlbumRight = findViewById(R.id.tvAlbumRight)
        tvYearRight = findViewById(R.id.tvYearRight)
        tvGenreRight = findViewById(R.id.tvGenreRight)
        tvCountryRight = findViewById(R.id.tvCountryRight)

        btnAddToPlaylist = findViewById(R.id.btnAddToPlaylist)
        btnPlay = findViewById(R.id.btnPlay)
        btnFavorite = findViewById(R.id.btnFavorite)
        tvProgressTime = findViewById(R.id.tvProgressTime)
        btnPlay.isEnabled = false

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
        tvTrackName.text = trackNameWithAlbum
        tvArtistName.text = track.artistName


        tvDurationRight.text = track.trackTime

        if (!track.collectionName.isNullOrEmpty()) {
            tvAlbumRight.text = track.collectionName
            tvAlbumRight.isVisible = true
        } else {
            tvAlbumRight.isVisible = false
        }

        if (!year.isNullOrEmpty()) {
            tvYearRight.text = year
            tvYearRight.isVisible = true
        } else {
            tvYearRight.isVisible = false
        }

        if (!track.primaryGenreName.isNullOrEmpty()) {
            tvGenreRight.text = track.primaryGenreName
            tvGenreRight.isVisible = true
        } else {
            tvGenreRight.isVisible = false
        }

        if (!track.country.isNullOrEmpty()) {
            tvCountryRight.text = track.country
            tvCountryRight.isVisible = true
        } else {
            tvCountryRight.isVisible = false
        }


        tvProgressTime.text = getString(R.string.progress_time_format)
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
            .into(ivCover)
    }

    private fun setupListeners() {
        tbAudioPlayer.setNavigationOnClickListener {
            finish()
        }
        btnPlay.setOnClickListener {
            playbackControl()

        }
        btnFavorite.setOnClickListener {
            Log.d("AudioPlayer", "Favorite clicked")

        }

        btnAddToPlaylist.setOnClickListener {
            Log.d("AudioPlayer", "Add to playlist clicked")

        }

    }



}