package com.noteapp.presentation.screens.detail;

import com.noteapp.domain.usecase.DeleteNoteUseCase;
import com.noteapp.domain.usecase.GetNoteByIdUseCase;
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
public final class NoteDetailViewModel_Factory implements Factory<NoteDetailViewModel> {
  private final Provider<GetNoteByIdUseCase> getNoteByIdUseCaseProvider;

  private final Provider<DeleteNoteUseCase> deleteNoteUseCaseProvider;

  public NoteDetailViewModel_Factory(Provider<GetNoteByIdUseCase> getNoteByIdUseCaseProvider,
      Provider<DeleteNoteUseCase> deleteNoteUseCaseProvider) {
    this.getNoteByIdUseCaseProvider = getNoteByIdUseCaseProvider;
    this.deleteNoteUseCaseProvider = deleteNoteUseCaseProvider;
  }

  @Override
  public NoteDetailViewModel get() {
    return newInstance(getNoteByIdUseCaseProvider.get(), deleteNoteUseCaseProvider.get());
  }

  public static NoteDetailViewModel_Factory create(
      Provider<GetNoteByIdUseCase> getNoteByIdUseCaseProvider,
      Provider<DeleteNoteUseCase> deleteNoteUseCaseProvider) {
    return new NoteDetailViewModel_Factory(getNoteByIdUseCaseProvider, deleteNoteUseCaseProvider);
  }

  public static NoteDetailViewModel newInstance(GetNoteByIdUseCase getNoteByIdUseCase,
      DeleteNoteUseCase deleteNoteUseCase) {
    return new NoteDetailViewModel(getNoteByIdUseCase, deleteNoteUseCase);
  }
}
