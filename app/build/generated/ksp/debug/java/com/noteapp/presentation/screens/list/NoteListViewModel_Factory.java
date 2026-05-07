package com.noteapp.presentation.screens.list;

import com.noteapp.domain.usecase.DeleteNoteUseCase;
import com.noteapp.domain.usecase.GetAllNotesUseCase;
import com.noteapp.domain.usecase.SearchNotesUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class NoteListViewModel_Factory implements Factory<NoteListViewModel> {
  private final Provider<GetAllNotesUseCase> getAllNotesUseCaseProvider;

  private final Provider<DeleteNoteUseCase> deleteNoteUseCaseProvider;

  private final Provider<SearchNotesUseCase> searchNotesUseCaseProvider;

  public NoteListViewModel_Factory(Provider<GetAllNotesUseCase> getAllNotesUseCaseProvider,
      Provider<DeleteNoteUseCase> deleteNoteUseCaseProvider,
      Provider<SearchNotesUseCase> searchNotesUseCaseProvider) {
    this.getAllNotesUseCaseProvider = getAllNotesUseCaseProvider;
    this.deleteNoteUseCaseProvider = deleteNoteUseCaseProvider;
    this.searchNotesUseCaseProvider = searchNotesUseCaseProvider;
  }

  @Override
  public NoteListViewModel get() {
    return newInstance(getAllNotesUseCaseProvider.get(), deleteNoteUseCaseProvider.get(), searchNotesUseCaseProvider.get());
  }

  public static NoteListViewModel_Factory create(
      Provider<GetAllNotesUseCase> getAllNotesUseCaseProvider,
      Provider<DeleteNoteUseCase> deleteNoteUseCaseProvider,
      Provider<SearchNotesUseCase> searchNotesUseCaseProvider) {
    return new NoteListViewModel_Factory(getAllNotesUseCaseProvider, deleteNoteUseCaseProvider, searchNotesUseCaseProvider);
  }

  public static NoteListViewModel newInstance(GetAllNotesUseCase getAllNotesUseCase,
      DeleteNoteUseCase deleteNoteUseCase, SearchNotesUseCase searchNotesUseCase) {
    return new NoteListViewModel(getAllNotesUseCase, deleteNoteUseCase, searchNotesUseCase);
  }
}
