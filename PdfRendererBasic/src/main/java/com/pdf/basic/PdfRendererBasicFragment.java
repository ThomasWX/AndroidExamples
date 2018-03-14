package com.pdf.basic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wb on 18-3-6.
 */

public class PdfRendererBasicFragment extends Fragment implements View.OnClickListener {

    private static final String STATE_CURRENT_PAGE_INDEX = "current_page_index";
    private static final String FILENAME = "超级吸引力.pdf";

    /**
     * File descriptor of the PDF.
     */
    private ParcelFileDescriptor mFileDescriptor;


    /**
     * {@link PdfRenderer} to render the PDF.
     */
    private PdfRenderer mPdfRenderer;


    /**
     * Page that is currently shown on the screen.
     */
    private PdfRenderer.Page mCurrentPage;

    // that shows a PDF page as a bitmap
    private ImageView mImageView;

    // to move to the previous page.
    private Button mPreviousButton;
    // to move to the next page.
    private Button mNextButton;

    /**
     * PDF page index
     */
    private int mPageIndex;

    public PdfRendererBasicFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pdf_renderer_basic, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageView = view.findViewById(R.id.image);
        mPreviousButton = view.findViewById(R.id.previous);
        mNextButton = view.findViewById(R.id.next);

        mPreviousButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);

        mPageIndex = 0;

        // If there is a savedInstanceState (screen orientations, etc.), we restore the page index.
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(STATE_CURRENT_PAGE_INDEX);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            openRenderer(getActivity());
            showPage(mPageIndex);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != mCurrentPage) {
            outState.putInt(STATE_CURRENT_PAGE_INDEX, mCurrentPage.getIndex());
        }
    }


    private void openRenderer(Context context) throws IOException {
        // In this sample, we read a PDF from the assets directory.
        File file = new File(context.getCacheDir(), FILENAME);
        if (!file.exists()) {

            // Since PdfRenderer cannot handle the compressed（压缩） asset file directly, we copy it into
            // the cache directory.

            InputStream asset = context.getAssets().open(FILENAME);
            FileOutputStream out = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                out.write(buffer, 0, size);
            }

            asset.close();
            out.close();

        }

        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        // This is the PdfRenderer we use to render the PDF.
        if (mFileDescriptor != null) {
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
        }


    }

    private void closeRenderer() throws IOException {
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        mPdfRenderer.close();
        mFileDescriptor.close();
    }

    private void showPage(int index) {
        if (mPdfRenderer.getPageCount() <= index) {
            return;
        }

        // Make sure to close the current page before opening another one.
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }

        // Use `openPage` to open a specific page in PDF.
        mCurrentPage = mPdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).

        Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(), Bitmap.Config.ARGB_8888);

        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        // We are ready to show the Bitmap to user.
        mImageView.setImageBitmap(bitmap);

        updateUi();
    }

    /**
     * Updates the state of 2 control buttons in response to the current page index.
     */
    private void updateUi() {
        int index = mCurrentPage.getIndex();
        int pageCount = mPdfRenderer.getPageCount();
        mPreviousButton.setEnabled(0 != index);// 首页不可用
        mNextButton.setEnabled(index + 1 < pageCount);// 末页不可用
        getActivity().setTitle(getString(R.string.app_name_with_index, index + 1, pageCount));
    }

    /**
     * Gets the number of pages in the PDF. This method is marked as public for testing.
     */

    public int getPageCount() {
        return mPdfRenderer.getPageCount();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous:
                // Move to the previous page
                showPage(mCurrentPage.getIndex() - 1);
                break;
            case R.id.next:
                // Move to the next page
                showPage(mCurrentPage.getIndex() + 1);
                break;
        }
    }
}
