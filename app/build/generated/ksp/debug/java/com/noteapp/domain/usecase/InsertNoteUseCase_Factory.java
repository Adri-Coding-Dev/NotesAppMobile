package com.noteapp.domain.usecase;

import com.noteapp.domain.repository.NoteRepository;
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
public final class InsertNoteUseCase_Factory implements Factory<InsertNoteUseCase> {
  private final Provider<NoteRepository> repositoryProvider;

  public InsertNoteUseCase_Factory(Provider<NoteRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public InsertNoteUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static InsertNoteUseCase_Factory create(Provider<NoteRepository> repositoryProvider) {
    return new InsertNoteUseCase_Factory(repositoryProvider);
  }

  public static InsertNoteUseCase newInstance(NoteRepository repository) {
    return new InsertNoteUseCase(repository);
  }
}
